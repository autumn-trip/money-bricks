package com.moneybricks.member.dto;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemberDTO extends User {
    private Long id;                // 회원 ID
    private String username;        // 아이디
    private String password;        // 비밀번호
    private String email;           // 이메일
    private String name;            // 이름
    private String nickname;        // 닉네임
    private String phoneNumber;     // 전화번호
    private String ssn;             // 주민등록번호
    private boolean emailAgreed;    // 이메일 수신 동의 여부
    private boolean socialLinked; // 소셜 계정과 연동 여부
    private boolean deleted;      // 회원 탈퇴 여부
    private boolean firstLoginFlag; // 처음 로그인 여부
    private List<String> memberRoles = List.of("USER");  // 역할 목록

    public MemberDTO(Long id, String username, String password,
                     String email, String name, String nickname,
                     String phoneNumber, String ssn, boolean emailAgreed,
                     boolean socialLinked, boolean deleted, boolean firstLoginFlag, List<String> memberRoles) {
        super(username, password, memberRoles.stream()
                .map(str -> new SimpleGrantedAuthority("ROLE_" + str)) // 권한 리스트
                .collect(Collectors.toList()));
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.ssn = ssn;
        this.emailAgreed = emailAgreed;
        this.socialLinked = socialLinked;
        this.deleted = deleted;
        this.firstLoginFlag = firstLoginFlag;
        this.memberRoles = memberRoles;
    }

    // 사용자 정보를 Map 형태로 반환
    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("id", id);
        dataMap.put("username", username);
        dataMap.put("password", password);
        dataMap.put("email", email);
        dataMap.put("name", name);
        dataMap.put("nickname", nickname);
        dataMap.put("phoneNumber", phoneNumber);
        dataMap.put("ssn", ssn);
        dataMap.put("emailAgreed", emailAgreed);
        dataMap.put("socialLinked", socialLinked);
        dataMap.put("deleted", deleted);
        dataMap.put("firstLoginFlag", firstLoginFlag);
        dataMap.put("memberRoles", memberRoles);

        return dataMap;
    }
}

