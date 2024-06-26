package com.team.RecipeRadar.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.RecipeRadar.domain.admin.domain.BlackListRepository;
import com.team.RecipeRadar.domain.member.application.AccountRetrievalService;
import com.team.RecipeRadar.domain.member.application.MemberService;
import com.team.RecipeRadar.domain.member.dao.MemberRepository;
import com.team.RecipeRadar.domain.member.dto.AccountRetrieval.FindLoginIdRequest;
import com.team.RecipeRadar.domain.member.dto.AccountRetrieval.FindPasswordRequest;
import com.team.RecipeRadar.domain.member.dto.AccountRetrieval.UpdatePasswordRequest;
import com.team.RecipeRadar.global.email.application.AccountRetrievalEmailServiceImpl;
import com.team.RecipeRadar.global.jwt.utils.JwtProvider;
import com.team.RecipeRadar.global.payload.ControllerApiResponse;
import com.team.RecipeRadar.global.security.oauth2.CustomOauth2Handler;
import com.team.RecipeRadar.global.security.oauth2.CustomOauth2Service;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountRetrievalController.class)
@ExtendWith(SpringExtension.class)
@Slf4j
class AccountRetrievalControllerTest {

    @MockBean
    private AccountRetrievalService accountRetrievalService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    BlackListRepository blackListRepository;

    @MockBean
    AccountRetrievalEmailServiceImpl mailService;
    @MockBean
    MemberRepository memberRepository;
    @MockBean
    MemberService memberService;
    @MockBean
    JwtProvider jwtProvider;
    @MockBean
    CustomOauth2Handler customOauth2Handler;
    @MockBean
    CustomOauth2Service customOauth2Service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("아이디 찾기 컨트롤러 테스트")
    void test() throws Exception{

        String username = "홍길동";
        String email="test@email.com";
        int code = 123456;


        FindLoginIdRequest findLoginIdDto = new FindLoginIdRequest(username, email, code);

        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("login_type","normal");
        map.put("login_type","keuye0638");
        mapList.add(map);

        given(accountRetrievalService.findLoginId(eq(username),eq(email),eq(code))).willReturn(mapList);

        mockMvc.perform(post("/api/search/login-id")
                .param("code",String.valueOf(code))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(findLoginIdDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.data[0]['login_type']").value("keuye0638"));
    }

    @Test
    @DisplayName("비밀번호 찾기 엔드포인트")
    void find_password() throws Exception {
        String username = "홍길동";
        String loginId = "loginId";
        String email = "test@email.com";
        int code = 123456;

        FindPasswordRequest findPasswordDto = new FindPasswordRequest(username, loginId, email, code);

        // 반환할 맵 설정
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("token", "test_TOken");
        map.put("회원 정보", true);
        map.put("이메일 인증", true);

        // accountRetrievalService.findPwd() 메서드가 호출될 때 반환할 값 설정
        given(accountRetrievalService.findPwd(findPasswordDto.getUsername(), findPasswordDto.getLoginId(), findPasswordDto.getEmail(), findPasswordDto.getCode())).willReturn(map);

        // 요청 및 응답 확인
        mockMvc.perform(post("/api/search/password")
                        .param("code",String.valueOf(code))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(findPasswordDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data['회원 정보']").value(true))
                .andExpect(jsonPath("$.data['이메일 인증']").value(true));
    }

    @Test
    @DisplayName("비밀번호 수정 엔드포인트")
    void update_password_controller() throws Exception {
        String loginId="loginId";
        String token = new String(Base64.getEncoder().encode("token".getBytes()));

        UpdatePasswordRequest updatePasswordDto = new UpdatePasswordRequest(loginId, "asdQWE123!@", "asdQWE123!@");
        ControllerApiResponse apiResponse = new ControllerApiResponse(true, "비밀번호 변경 성공");
        given(accountRetrievalService.updatePassword(updatePasswordDto,token)).willReturn(apiResponse);

        Cookie cookie = new Cookie("account-token", token);

        mockMvc.perform(put("/api/password/update")
                        .cookie(cookie)
                .content(objectMapper.writeValueAsString(updatePasswordDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("비밀번호 변경 성공"))
                .andExpect(jsonPath("$.success").value(true));
    }
}