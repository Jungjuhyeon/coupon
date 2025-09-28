package com.personal_project.coupon.store.application.inputport;

import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import com.personal_project.coupon.store.application.outputport.MenuCategoryOutputPort;
import com.personal_project.coupon.store.application.outputport.MenuOutputPort;
import com.personal_project.coupon.store.application.outputport.StoreOutputPort;
import com.personal_project.coupon.store.application.usecase.AddMenuUseCase;
import com.personal_project.coupon.store.domain.model.Menu;
import com.personal_project.coupon.store.domain.model.MenuCategory;
import com.personal_project.coupon.store.domain.model.Store;
import com.personal_project.coupon.store.framwork.web.request.MenuListDTO;
import com.personal_project.coupon.store.framwork.web.response.MenuOutputDTO;
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
                .orElseThrow(()-> new BusinessException(CommonErrorCode.STORE_NOT_FOUND));

        List<Menu> menuList = request.getList().stream()
                .map(dto ->{
                    MenuCategory menuCategory = menuCategoryOutputPort.findById(dto.getMenuCategoryId())
                            .orElseThrow(()-> new BusinessException(CommonErrorCode.MENU_CATEGORY_NOT_FOUND));
                    return Menu.create(store,menuCategory,dto.getName(),dto.getPrice());
                }).toList();

        List<Menu> newMenuList = menuOutputPort.saveAll(menuList);
        return MenuOutputDTO.mapToDTO(store.getId(),newMenuList);
    }

}
