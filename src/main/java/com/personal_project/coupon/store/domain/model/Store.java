package com.personal_project.coupon.store.domain.model;


import com.personal_project.coupon.global.entity.BaseEntity;
import com.personal_project.coupon.member.domain.Member;
import com.personal_project.coupon.store.domain.model.enumeration.StoreStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_category_id")
    private StoreCategory storeCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Menu> menuList = new ArrayList<>();

    private String name;

    private String storePhoneNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    private Store(Brand brand,StoreCategory storeCategory,Member owner,String name,String phone,String address){
        this.brand =brand;
        this.storeCategory =storeCategory;
        this.owner = owner;
        this.name = name;
        this.storePhoneNumber = phone;
        this.address = address;
        this.status = StoreStatus.OPEN;
    }
    public static Store create(Brand brand,StoreCategory storeCategory,Member owner,String name,String phone,String address){
        return new Store(brand,storeCategory,owner,name,phone,address);
    }

}
