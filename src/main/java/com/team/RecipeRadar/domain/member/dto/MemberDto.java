package com.team.RecipeRadar.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.team.RecipeRadar.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDto {

    @Schema(nullable = true,hidden = true)
    Long id;


    @NotEmpty(message = "이름을 입력주세요")
    @Pattern(regexp = "^[가-힣]+.{1,}$",message = "이름을 정확이 입력해주세요")
    @Schema(description = "사용자 실명",example = "홍길동")
    String username;
    
    @NotEmpty(message = "별명을 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{4,}$",message = "사용할수 없는 별명입니다.")
    @Schema(description = "사용자의 별명",example = "나만냉")
    String nickname;


    @NotEmpty(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[`~!@#$%^&*()_+])(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,16}$",message = "사용할수 없는 비밀번호 입니다.")
    @Schema(description = "비밀번호",example = "asdASD12!@")
    String password;


    @NotEmpty(message = "비밀번호를 다시한번 입력해주세요")
    @Schema(description = "비밀번호 재입력",example = "asdASD12!@")
    String passwordRe;

    @NotEmpty(message = "아이디를 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,16}$", message = "올바른 아이디를 입력해주세요")
    @Schema(description = "로그인 아이디",example = "exampleId")
    String loginId;


    @NotEmpty(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.(com|net)$", message = "올바른 이메일 형식이어야 합니다.")
    @Schema(description = "이메일",example = "test@naver.com")
    String email;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String roles;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDate join_date;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String login_type;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean verified;

    @JsonIgnore
    String code;

    public static Member toEntity(MemberDto memberDto) {
        return Member.builder()
                .id(memberDto.getId())
                .loginId(memberDto.loginId)
                .email(memberDto.email)
                .join_date(memberDto.join_date)
                .nickName(memberDto.nickname)
                .username(memberDto.getUsername())
                .login_type(memberDto.getLogin_type())
                .build();
    }

    private MemberDto(Long memberId, String loginId, String email, String username, String nickname, LocalDate join_date) {
        this.id = memberId;
        this.loginId = loginId;
        this.email = email;
        this.username = username;
        this.nickname =nickname;
        this.join_date = join_date;
    }

    public static MemberDto from(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .username(member.getUsername())
                .nickname(member.getNickName())
                .password(member.getPassword())
                .email(member.getEmail())
                .loginId(member.getLoginId())
                .roles(member.getRoles())
                .login_type(member.getLogin_type()).build();
    }

    public static MemberDto of(Long memberId, String loginId, String email, String username, String nickname, LocalDate join_date){
        return new MemberDto(memberId, loginId, email, username, nickname, join_date);
    }
}
