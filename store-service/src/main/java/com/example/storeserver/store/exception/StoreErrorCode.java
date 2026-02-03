package com.example.storeserver.store.exception;

import com.example.common.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements ErrorCode {

    MEMBER_SERVICE_UNAVAILABLE(503, "M-4001", "회원 서비스가 일시적으로 이용 불가능합니다."),
    //store error(4301 ~ 4400)
    STORE_NOT_FOUND(404,"S-4301","해당 가게는 존재하지 않습니다."),
    //store_category error(4401 ~ 4450)
    STORE_CATEGORY_NOT_FOUND(404,"C-4401","해당 카테고리는 존재하지 않습니다."),
    //brand error(4451 ~ 4500)
    BRAND_NOT_FOUND(404,"B-4451","해당 브랜드는 존재하지 않습니다."),
    //menu error(4551 ~ 4600)
    MENU_NOT_FOUND(404,"M-4551","해당 메뉴는 존재하지 않습니다."),
    MENU_CATEGORY_NOT_FOUND(404,"M-4552","해당 메뉴카테고리는 존재하지 않습니다."),

    OWNER_NOT_FOUND(400, "S-4001", "가게의 주인이 유효하지 않습니다.");

    private final Integer httpStatus;
    private final String code;
    private final String message;
}
