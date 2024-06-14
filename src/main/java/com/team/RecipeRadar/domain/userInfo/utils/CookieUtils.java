package com.team.RecipeRadar.domain.userInfo.utils;

import com.team.RecipeRadar.domain.member.dao.AccountRetrievalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class CookieUtils {

    private final AccountRetrievalRepository accountRetrievalRepository;

    public ResponseCookie createCookie(String cookieName, String value, int expiredTime){

        String userEncodeToken = new String(Base64.getEncoder().encode(value.getBytes()));

        ResponseCookie responseCookie = ResponseCookie.from(cookieName, userEncodeToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(expiredTime)
                .build();
        return responseCookie;
    }

    public boolean validCookie(String cookieValue,String loginId){
        String decodeCookie = new String(Base64.getDecoder().decode(cookieValue.getBytes()));
        boolean existCookie = accountRetrievalRepository.existsByLoginIdAndVerificationId(loginId, decodeCookie);
        return existCookie;
    }

    public ResponseCookie deleteCookie(String cookieName){
        return ResponseCookie.from(cookieName,null)
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .maxAge(0).path("/").build();
    }
}