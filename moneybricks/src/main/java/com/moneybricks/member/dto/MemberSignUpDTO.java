package com.moneybricks.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpDTO {
    @NotBlank(message = "아이디는 필수 입력값입니다")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다")
    private String confirmPassword;

    @NotBlank(message = "이메일은 필수 입력값입니다")
    private String email;

    @NotBlank(message = "이름은 필수 입력값입니다")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력값입니다")
    private String nickname;

    @NotBlank(message = "전화번호는 필수 입력값입니다")
    private String phoneNumber;

    @NotBlank(message = "주민등록번호는 필수 입력값입니다")
    private String ssn;

    private boolean emailAgreed;
}
