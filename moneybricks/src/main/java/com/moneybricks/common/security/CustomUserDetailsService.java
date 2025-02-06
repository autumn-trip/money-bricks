package com.moneybricks.common.security;

import com.moneybricks.member.domain.Member;
import com.moneybricks.member.dto.MemberDTO;
import com.moneybricks.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {   // 실제 인증 처리하는 인터페이스

    private final MemberRepository memberRepository;

    @Override   // 실제 인증을 처리할 때 호출하는 메서드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("----------------loadUserByUsername-----------------------------");
        log.info("username : " + username);

        // 사용자와 권한 정보 조회
        Member member = memberRepository.getWithRoles(username);

        // 회원이 없거나 탈퇴한 회원인 경우 로그인 차단
        if (member == null || member.isDeleted()) {
            log.error("User not found for username: " + username);
            throw new UsernameNotFoundException("사용할 수 없는 계정입니다.");
        }

        log.info("Member found: " + member);

        // MemberDTO 생성
        MemberDTO memberDTO = new MemberDTO(
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
                member.isFirstLoginFlag(),
                member.getMemberRoleList()
                        .stream()
                        .map(memberRole -> memberRole.name()) // Enum 값을 문자열로 변환
                        .collect(Collectors.toList())); // 권한 리스트 수집

        log.info("Converted MemberDTO: " + memberDTO);

        return memberDTO;
    }
}
