package com.moneybricks.common.security;

import com.moneybricks.common.security.dto.MemberSecurityDTO;
import com.moneybricks.member.domain.Member;
import com.moneybricks.member.domain.SocialAccount;
import com.moneybricks.member.repository.MemberRepository;
import com.moneybricks.member.repository.SocialAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final SocialAccountRepository socialAccountRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest...........");
        log.info(userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        log.info("Client Name: " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String socialEmail = null;
        String socialId = null;

        switch (clientName) {
            case "kakao":
                socialEmail = getKakao(paramMap);
                socialId = paramMap.get("sub").toString();
                break;
        }

        log.info("=================================");
        log.info("Social ID: " + socialId);
        log.info("Social Email: " + socialEmail);
        log.info("=================================");

        // 1. 소셜 계정 존재 여부 확인
        Optional<SocialAccount> existingSocialAccount = socialAccountRepository.findBySocialId(socialId);

        if (existingSocialAccount.isPresent()) {
            // 이미 연동된 소셜 계정이 있는 경우
            Member member = existingSocialAccount.get().getMember();
            return createMemberSecurityDTO(member, paramMap);
        }

        // 2. 이메일로 기존 회원 확인
        Optional<Member> existingMember = memberRepository.findByEmail(socialEmail);

        if (existingMember.isPresent()) {
            // 기존 회원이 있는 경우 -> 소셜 계정 연동
            Member member = existingMember.get();
            linkSocialAccount(member, socialId, socialEmail);
            return createMemberSecurityDTO(member, paramMap);
        }

        // 3. 신규 소셜 로그인 시도 -> 회원가입 필요
        throw new OAuth2AuthenticationException("회원가입이 필요합니다.");
    }

    private String getKakao(Map<String, Object> paramMap) {
        log.info("KAKAO-----------------------------------------");

        Object value = paramMap.get("kakao_account");
        if (value == null) {
            log.error("Kakao account info is missing");
            return null;
        }

        log.info("Kakao account info: " + value);

        if (value instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) value;
            String email = (String) accountMap.get("email");
            log.info("Email: " + email);
            return email;
        } else {
            log.error("Invalid kakao_account format");
            return null;
        }
    }

    private void linkSocialAccount(Member member, String socialId, String socialEmail) {
        member.changeSocialLinked(true);
        memberRepository.save(member);

        SocialAccount socialAccount = SocialAccount.builder()
                .socialId(socialId)
                .socialEmail(socialEmail)
                .member(member)
                .build();
        socialAccountRepository.save(socialAccount);
    }

    private MemberSecurityDTO createMemberSecurityDTO(Member member, Map<String, Object> props) {
        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getEmail(),
                member.getName(),
                member.getNickname(),
                member.getPhoneNumber(),
                member.getSsn(),
                member.isEmailAgreed(),
                member.isSocialLinked(),
                member.isDeleted(),
                member.getMemberRoleList().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList())
        );

        memberSecurityDTO.setProps(props);
        return memberSecurityDTO;
    }
}