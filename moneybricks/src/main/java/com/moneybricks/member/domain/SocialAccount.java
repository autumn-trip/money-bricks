package com.moneybricks.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "social_accounts")
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 소셜 계정 고유 ID

    @Column(nullable = false, unique = true)
    private String socialId;  // 카카오 고유 ID (sub 값)

    @Column(nullable = false, unique = true)
    private String socialEmail;  // 카카오 이메일

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Member member;  // 해당 소셜 계정과 연결된 멤버
}
