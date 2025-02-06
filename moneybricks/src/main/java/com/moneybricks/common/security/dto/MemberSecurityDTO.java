package com.moneybricks.common.security.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class MemberSecurityDTO extends User implements OAuth2User {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
    private String ssn;
    private boolean emailAgreed;
    private boolean socialLinked;  // 소셜 계정 연동 여부
    private boolean deleted;

    private Map<String, Object> props; // 소셜 로그인 정보 (여기서는 OAuth2User에서 제공하는 속성)

    public MemberSecurityDTO(Long id, String username, String password, String email,
                             String name, String nickname, String phoneNumber, String ssn,
                             boolean emailAgreed, boolean socialLinked, boolean deleted,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
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
    }

    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

}
