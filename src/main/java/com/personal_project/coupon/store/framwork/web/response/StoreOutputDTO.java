package com.personal_project.coupon.store.framwork.web.response;

import com.personal_project.coupon.store.domain.model.Store;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreOutputDTO {
    private Long storeId;

    public static StoreOutputDTO mapToDTO(Store store){
        return StoreOutputDTO.builder()
                .storeId(store.getId())
                .build();
    }

}
