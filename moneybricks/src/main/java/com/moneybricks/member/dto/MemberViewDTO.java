package com.moneybricks.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 회원 정보 확인용 DTO
public class MemberViewDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
    private String ssn;
    private Boolean emailAgreed;
    private Boolean socialLinked;
}

