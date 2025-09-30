package com.personal_project.coupon.store.domain.model;

import com.personal_project.coupon.global.entity.BaseEntity;
import com.personal_project.coupon.store.domain.model.enumeration.MenuStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_category_id")
    private MenuCategory menuCategory;

    private String name;

    private Integer price;

    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;

    private Menu(Store store, MenuCategory menuCategory,String name, Integer price) {
        this.store = store;
        this.menuCategory = menuCategory;
        this.name = name;
        this.price = price;
        this.menuStatus = MenuStatus.AVAILABLE;
    }
    public static Menu create(Store store,MenuCategory menuCategory, String name, Integer price){
        return new Menu(store,menuCategory,name,price);
    }

}
