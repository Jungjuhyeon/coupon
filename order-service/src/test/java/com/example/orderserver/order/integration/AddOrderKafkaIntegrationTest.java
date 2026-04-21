package com.example.orderserver.order.integration;

import com.example.orderserver.order.application.outputport.CouponOutputPort;
import com.example.orderserver.order.application.outputport.MemberOutputPort;
import com.example.orderserver.order.application.outputport.PaymentOutputPort;
import com.example.orderserver.order.application.outputport.StoreOutputPort;
import com.example.orderserver.order.application.usecase.AddOrderUseCase;
import com.example.orderserver.order.framework.web.request.OrderInputDTO;
import com.example.orderserver.order.framework.web.request.OrderMenuInfoDTO;
import com.example.orderserver.order.infra.store.dto.response.MenuInfoFeignDTO;
import com.example.orderserver.order.infra.store.dto.response.StoreOrderViewFeignDTO;
import org.springframework.test.util.ReflectionTestUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "eureka.client.register-with-eureka=false",
                "eureka.client.fetch-registry=false",
                "spring.data.redis.cluster.nodes=",
                "management.health.redis.enabled=false"
        }
)
@Testcontainers
class AddOrderKafkaIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.4.0")
    );

    @Container
    static MongoDBContainer mongodb = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        // MySQL
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        // Kafka
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        // MongoDB
        registry.add("spring.data.mongodb.host", mongodb::getHost);
        registry.add("spring.data.mongodb.port", () -> String.valueOf(mongodb.getMappedPort(27017)));
        registry.add("spring.data.mongodb.database", () -> "orders");
        // Redis: 이 테스트에서 Redis 연산 없음 — MockBean으로 대체, 프로퍼티만 dummy 설정
        registry.add("spring.data.redis.host", () -> "localhost");
        registry.add("spring.data.redis.port", () -> "6379");
    }

    // Redis 연결 없이 컨텍스트 구성 (Redis 연산 미사용)
    @MockBean RedisConnectionFactory redisConnectionFactory;
    @MockBean ReactiveRedisConnectionFactory reactiveRedisConnectionFactory;

    // 외부 Feign 호출 차단
    @MockBean MemberOutputPort memberOutputPort;
    @MockBean StoreOutputPort storeOutputPort;
    @MockBean PaymentOutputPort paymentOutputPort;
    @MockBean CouponOutputPort couponOutputPort;

    @Autowired AddOrderUseCase addOrderUseCase;

    @BeforeEach
    void setUp() {
        // OrderMenuFactory → storeOutputPort.getMenuInfoList() → null이면 NPE
        when(storeOutputPort.getMenuInfoList(anyList()))
                .thenReturn(List.of(new MenuInfoFeignDTO(1L, 10000)));

        // OrderCreatedConsumer (Kafka consumer) 가 storeOrderView 조회 → NPE 방지
        when(storeOutputPort.getRequiredStoreOrderView(anyLong(), anyList()))
                .thenReturn(new StoreOrderViewFeignDTO("테스트 가게", "테스트 브랜드", "한식", List.of()));
    }

    @Test
    @DisplayName("주문 생성 후 orders_created 토픽에 OrderCreatedEvent 메시지 발행 확인")
    void create_order_publishes_OrderCreatedEvent_to_kafka() throws Exception {
        // given: DTO에 생성자 없음 → ReflectionTestUtils로 필드 직접 설정
        OrderMenuInfoDTO menuInfoDTO = new OrderMenuInfoDTO();
        ReflectionTestUtils.setField(menuInfoDTO, "menuId", 1L);
        ReflectionTestUtils.setField(menuInfoDTO, "count", 2);

        OrderInputDTO request = new OrderInputDTO();
        ReflectionTestUtils.setField(request, "couponIssueId", null);
        ReflectionTestUtils.setField(request, "deliveryAddress", "서울시 강남구 테헤란로 123");
        ReflectionTestUtils.setField(request, "comment", "문 앞에 놔주세요");
        ReflectionTestUtils.setField(request, "orderMenuInfoDTOList", List.of(menuInfoDTO));

        // Kafka 검증용 컨슈머 (test 전용 group-id)
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-verification-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(List.of("orders_created"));

            // when: 주문 생성 (@TransactionalEventListener AFTER_COMMIT 이후 Kafka 발행)
            addOrderUseCase.create(1L, 1L, request);

            // then: 최대 15초 대기 후 메시지 수신 확인
            ConsumerRecords<String, String> records = ConsumerRecords.empty();
            long deadline = System.currentTimeMillis() + 15_000;
            while (records.isEmpty() && System.currentTimeMillis() < deadline) {
                records = consumer.poll(Duration.ofSeconds(2));
            }

            assertThat(records.isEmpty()).isFalse();
            String payload = records.iterator().next().value();
            assertThat(payload).contains("orderId");
            assertThat(payload).contains("memberId");
            assertThat(payload).contains("OrderCreated");
        }
    }
}
