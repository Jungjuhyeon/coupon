package com.example.storeserver.store.application.inputport;


import com.example.common.global.exception.BusinessException;
import com.example.storeserver.store.application.outputport.MenuOutputPort;
import com.example.storeserver.store.application.usecase.InquiryMenuUseCase;
import com.example.storeserver.store.domain.model.Menu;
import com.example.storeserver.store.exception.StoreErrorCode;
import com.example.storeserver.store.framework.web.response.MenuInfoFeignDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryMenuInputPort implements InquiryMenuUseCase {
    private final MenuOutputPort menuOutputPort;
    @Override
    public List<MenuInfoFeignDTO> getMenuIdAndPrice(List<Long> menuIds){
        List<Menu> menus = menuOutputPort.findAllById(menuIds);

        if (menus.size() != menuIds.size()) {
            throw new BusinessException(StoreErrorCode.MENU_NOT_FOUND);
        }

        return menus.stream()
                .map(m -> MenuInfoFeignDTO.mapToDTO(m.getId(), m.getPrice()))
                .toList();
    }

}
