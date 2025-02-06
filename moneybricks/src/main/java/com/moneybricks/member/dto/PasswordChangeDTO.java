package com.moneybricks.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDTO {

    @NotBlank(message = "새 비밀번호는 필수 입력값입니다")
    private String password;

    @NotBlank(message = "새 비밀번호 확인은 필수 입력값입니다")
    private String confirmPassword;
}