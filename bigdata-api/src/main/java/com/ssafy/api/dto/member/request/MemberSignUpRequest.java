package com.ssafy.api.dto.member.request;

import com.ssafy.api.entity.mysql.Member;
import com.ssafy.api.entity.mysql.enums.MemberPrivilege;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Collections;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequest {
    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;
    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    private String password;
    @NotEmpty(message = "이름은 필수 입력값입니다.")
    private String name;

    public Member toEntity(String password){
        Member member = Member
                .builder()
                .email(email)
                .password(password)
                .name(name)
                .privilege(Collections.singleton(MemberPrivilege.GENERAL.name())).build();
        return member;
    }
}
