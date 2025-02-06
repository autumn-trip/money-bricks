package com.moneybricks.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 회원 검색용 DTO
public class MemberSearchDTO {
    private String username;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
    private Boolean socialLinked;
    private Boolean deleted;
}
