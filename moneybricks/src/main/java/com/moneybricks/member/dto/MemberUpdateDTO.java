package com.moneybricks.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDTO {

    @NotBlank(message = "이름은 필수 입력값입니다")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력값입니다")
    private String nickname;

    @NotBlank(message = "전화번호는 필수 입력값입니다")
    private String phoneNumber;

    private Boolean emailAgreed;  // 이메일 수신 동의 변경 (선택)
}
