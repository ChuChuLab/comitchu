package com.commi.chu.global.util;

import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;
import com.commi.chu.global.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationUtil {

    public Integer getCurrentUserId(UserPrincipal userPrincipal) {
        if (userPrincipal == null || userPrincipal.getId() == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        return userPrincipal.getId();
    }
}