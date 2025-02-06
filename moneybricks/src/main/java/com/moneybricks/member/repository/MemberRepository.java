package com.moneybricks.member.repository;

import com.moneybricks.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"memberRoleList"})
    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.memberRoleList WHERE m.username = :username")
    Member getWithRoles(@Param("username") String username); // 로그인 아이디(username)를 기준으로 조회

    Optional<Member> findByUsernameAndDeletedFalse(String username);

    Optional<Member> findByEmail(String socialEmail);

    boolean existsByNicknameAndDeletedFalse(String nickname);

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByUsernameAndDeletedFalse(String username);
}
