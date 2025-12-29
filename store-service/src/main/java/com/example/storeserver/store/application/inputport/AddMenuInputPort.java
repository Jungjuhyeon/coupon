package com.example.storeserver.store.application.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.storeserver.store.application.outputport.MenuCategoryOutputPort;
import com.example.storeserver.store.application.outputport.MenuOutputPort;
import com.example.storeserver.store.application.outputport.StoreOutputPort;
import com.example.storeserver.store.application.usecase.AddMenuUseCase;
import com.example.storeserver.store.exception.StoreErrorCode;
import com.example.storeserver.store.domain.model.Menu;
import com.example.storeserver.store.domain.model.MenuCategory;
import com.example.storeserver.store.domain.model.Store;
import com.example.storeserver.store.framwork.web.request.MenuListDTO;
import com.example.storeserver.store.framwork.web.response.MenuOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddMenuInputPort implements AddMenuUseCase {
    private final MenuOutputPort menuOutputPort;
    private final StoreOutputPort storeOutputPort;
    private final MenuCategoryOutputPort menuCategoryOutputPort;

    @Override
    @Transactional
    public MenuOutputDTO create(MenuListDTO request){
        Store store = storeOutputPort.findById(request.getStoreId())
                .orElseThrow(()-> new BusinessException(StoreErrorCode.STORE_NOT_FOUND));

        List<Menu> menuList = request.getList().stream()
                .map(dto ->{
                    MenuCategory menuCategory = menuCategoryOutputPort.findById(dto.getMenuCategoryId())
                            .orElseThrow(()-> new BusinessException(StoreErrorCode.MENU_CATEGORY_NOT_FOUND));
                    return Menu.create(store,menuCategory,dto.getName(),dto.getPrice());
                }).toList();

        List<Menu> newMenuList = menuOutputPort.saveAll(menuList);
        return MenuOutputDTO.mapToDTO(store.getId(),newMenuList);
    }

}
