package com.team.RecipeRadar.domain.member.application;

import com.team.RecipeRadar.domain.member.dto.MemberDto;
import com.team.RecipeRadar.domain.member.domain.Member;

import java.util.Map;

public interface MemberService {

    Member saveEntity(Member member);

    void saveDto(MemberDto memberDto);

    MemberDto findByLoginId(String loginId);

    Map<String, Boolean> LoginIdValid(String loginId);

    Map<String, Boolean> emailValid(String email);
    Map<String, Boolean> userNameValid(String username);
    Map<String, Boolean> checkPasswordStrength(String password);
    boolean ValidationOfSignUp(MemberDto memberDto,int code);
    Map<String, Boolean> duplicatePassword(String password,String passwordRe);

    Map<String,Boolean> nickNameValid(String nickName);
    Map<String, Boolean> verifyCode(String email,int code);

    void deleteMember(Long memberId);

}
