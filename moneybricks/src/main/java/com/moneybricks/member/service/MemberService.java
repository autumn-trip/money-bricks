package com.moneybricks.member.service;

import com.moneybricks.member.dto.MemberViewDTO;
import com.moneybricks.member.dto.MemberSignUpDTO;
import com.moneybricks.member.dto.MemberUpdateDTO;
import com.moneybricks.member.dto.PasswordChangeDTO;

import java.util.Map;

public interface MemberService {
    void registerMember(MemberSignUpDTO memberSignUpDTO);

    void updateMember(String username, MemberUpdateDTO updateDTO);

    void deleteMember(String username);

    MemberViewDTO getMemberInfo(String username);

    void changePassword(String username, PasswordChangeDTO passwordChangeDTO);

    Map<String, String> checkDuplicate(Map<String, String> params);

    // 비밀번호 확인
    boolean verifyPassword(String username, String password);

    // 첫 로그인 시 `firstLoginFlag`를 false로 변경하는 메서드
    void updateFirstLoginFlagToFalse(String username);
}
