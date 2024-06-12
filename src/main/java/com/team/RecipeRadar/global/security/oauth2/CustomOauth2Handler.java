package com.team.RecipeRadar.global.security.oauth2;


import com.team.RecipeRadar.global.jwt.utils.JwtProvider;
import com.team.RecipeRadar.global.security.basic.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2Handler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Value("${host.path}")
    private String successUrl;

    //소셜 로그인 성공시 해당로직을 타게되며 accessToken 과 RefreshToken을 발급해준다.
    // 소셜 로그인 성공 시 해당 로직을 타게 되며 accessToken과 RefreshToken을 발급해준다.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("onAuthenticationSuccess실행");
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        String loginId = principal.getMember().getLoginId();

        String jwtToken = jwtProvider.generateAccessToken(loginId);
        String refreshToken = jwtProvider.generateRefreshToken(loginId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(successUrl);

//        String redirectURI = builder
//                .queryParam("access-token", jwtToken)
//                .build().toString();

        ResponseCookie responseCookie = ResponseCookie.from("RefreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .build();

//        response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
//        response.sendRedirect(redirectURI);

        handleRedirect(successUrl,responseCookie,jwtToken);

    }

    public ResponseEntity<Void> handleRedirect(String redirectUrl,ResponseCookie responseCookie,String jwtToken) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.SET_COOKIE,responseCookie.toString())
                .location(URI.create(redirectUrl+jwtToken))
                .build();
    }

}
