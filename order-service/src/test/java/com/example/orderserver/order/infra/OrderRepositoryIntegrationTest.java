package com.example.orderserver.order.infra;

import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.enumeration.OrderStatus;
import com.example.orderserver.order.infra.persistence.OrderJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@EnableJpaAuditing
class OrderRepositoryIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Test
    @DisplayName("주문 저장 및 조회 성공")
    void saveOrder_and_findById() {
        // given
        Order order = Order.create(1L, 1L, "서울시 강남구 테헤란로", "문 앞에 놔주세요");

        // when
        Order saved = orderJpaRepository.save(order);
        Optional<Order> found = orderJpaRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getMemberId()).isEqualTo(1L);
        assertThat(found.get().getStoreId()).isEqualTo(1L);
        assertThat(found.get().getOrderStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("주문 취소 후 상태 CANCELLED 확인")
    void cancelOrder_status_cancelled() {
        // given
        Order order = Order.create(1L, 1L, "서울시 강남구 테헤란로", "빠르게 부탁드려요");
        Order saved = orderJpaRepository.save(order);

        // when
        saved.cancel();
        orderJpaRepository.save(saved);
        Optional<Order> found = orderJpaRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
    }
}
