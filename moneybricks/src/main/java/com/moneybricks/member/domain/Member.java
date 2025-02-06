package com.moneybricks.member.domain;

import com.moneybricks.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "members", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_nickname", columnList = "nickname")
})
@ToString(exclude = "memberRoleList")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 아이디 (로그인용)

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false, length = 100)
    private String name; // 이름

    @Column(nullable = false, unique = true, length = 100)
    private String nickname; // 닉네임

    @Column(nullable = false, unique = true, length = 11)
    private String phoneNumber; // 전화번호 (01000000000 형태)

    @Column(nullable = false, length = 7)
    private String ssn; // 주민등록번호 (0000000 형태만 필요)

    @Column(nullable = false)
    @Builder.Default
    private boolean emailAgreed = false; // 이메일 수신 동의 여부 (기본값 FALSE)

    @Column(nullable = false)
    @Builder.Default
    private boolean socialLinked  = false; // 소셜 계정과 연동 여부 (기본값 FALSE)

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false; // 회원 탈퇴 여부 (기본값 FALSE)

    @Column(nullable = false)
    @Builder.Default
    private boolean firstLoginFlag = true; // 처음 로그인 여부 (기본값 true)

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "members_id"))
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    // 역할 추가 메서드
    public void addRole(MemberRole memberRole) {
        if (!memberRoleList.contains(memberRole)) {
            memberRoleList.add(memberRole);
        }
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeEmailAgreed(boolean emailAgreed) {
        this.emailAgreed = emailAgreed;
    }

    public void changeSocialLinked(boolean socialLinked) {
        this.socialLinked = socialLinked;
    }

    public void changeDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void changeFirstLoginFlag(boolean firstLoginFlag) { this.firstLoginFlag = firstLoginFlag; }
}
