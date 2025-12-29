package com.example.storeserver.store.framwork.web.response;

import com.example.storeserver.store.domain.model.Store;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreIdOutputDTO {
    private Long storeId;

    public static StoreIdOutputDTO mapToDTO(Store store){
        return StoreIdOutputDTO.builder()
                .storeId(store.getId())
                .build();
    }

}
