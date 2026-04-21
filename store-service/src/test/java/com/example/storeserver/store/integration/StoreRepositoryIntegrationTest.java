package com.example.storeserver.store.integration;

import com.example.storeserver.store.application.dto.StoreBasicInfo;
import com.example.storeserver.store.domain.model.Brand;
import com.example.storeserver.store.domain.model.Menu;
import com.example.storeserver.store.domain.model.MenuCategory;
import com.example.storeserver.store.domain.model.Store;
import com.example.storeserver.store.domain.model.StoreCategory;
import com.example.storeserver.store.infra.persistence.BrandJpaRepository;
import com.example.storeserver.store.infra.persistence.MenuCategoryJpaRepository;
import com.example.storeserver.store.infra.persistence.MenuJpaRepository;
import com.example.storeserver.store.infra.persistence.StoreCategoryJpaRepository;
import com.example.storeserver.store.infra.persistence.StoreJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
class StoreRepositoryIntegrationTest {

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
    private TestEntityManager entityManager;

    @Autowired
    private StoreCategoryJpaRepository storeCategoryJpaRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private StoreJpaRepository storeJpaRepository;

    @Autowired
    private MenuCategoryJpaRepository menuCategoryJpaRepository;

    @Autowired
    private MenuJpaRepository menuJpaRepository;

    @Test
    @DisplayName("가게 저장 및 FK 관계 포함 조회 성공")
    void saveStore_and_findById() {
        // given
        StoreCategory storeCategory = storeCategoryJpaRepository.save(
                StoreCategory.builder().name("한식").build()
        );
        Brand brand = brandJpaRepository.save(
                Brand.builder().storeCategory(storeCategory).name("테스트 브랜드").build()
        );
        Store store = Store.create(brand, storeCategory, 1L, "테스트 가게", "02-1234-5678", "서울시 강남구");

        // when
        Store saved = storeJpaRepository.save(store);
        entityManager.flush();
        entityManager.clear();
        Optional<Store> found = storeJpaRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("테스트 가게");
        assertThat(found.get().getOwnerId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findStoreBasicInfo() - 커스텀 JPQL로 가게 기본 정보 조회 성공")
    void findStoreBasicInfo_returns_correct_projection() {
        // given
        StoreCategory storeCategory = storeCategoryJpaRepository.save(
                StoreCategory.builder().name("한식").build()
        );
        Brand brand = brandJpaRepository.save(
                Brand.builder().storeCategory(storeCategory).name("테스트 브랜드").build()
        );
        Store store = storeJpaRepository.save(
                Store.create(brand, storeCategory, 1L, "테스트 가게", "02-1234-5678", "서울시 강남구")
        );
        entityManager.flush();
        entityManager.clear();

        // when
        Optional<StoreBasicInfo> result = storeJpaRepository.findStoreBasicInfo(store.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getStoreName()).isEqualTo("테스트 가게");
        assertThat(result.get().getBrandName()).isEqualTo("테스트 브랜드");
        assertThat(result.get().getCategoryName()).isEqualTo("한식");
    }

    @Test
    @DisplayName("메뉴 저장 및 가게-메뉴 FK 관계 조회 성공")
    void saveMenu_and_findById() {
        // given
        StoreCategory storeCategory = storeCategoryJpaRepository.save(
                StoreCategory.builder().name("한식").build()
        );
        Brand brand = brandJpaRepository.save(
                Brand.builder().storeCategory(storeCategory).name("테스트 브랜드").build()
        );
        Store store = storeJpaRepository.save(
                Store.create(brand, storeCategory, 1L, "테스트 가게", "02-1234-5678", "서울시 강남구")
        );
        MenuCategory menuCategory = menuCategoryJpaRepository.save(
                MenuCategory.builder().storeCategory(storeCategory).name("메인 메뉴").build()
        );
        Menu menu = Menu.create(store, menuCategory, "불고기", 12000);

        // when
        Menu savedMenu = menuJpaRepository.save(menu);
        entityManager.flush();
        entityManager.clear();
        Optional<Menu> found = menuJpaRepository.findById(savedMenu.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("불고기");
        assertThat(found.get().getPrice()).isEqualTo(12000);
    }
}
