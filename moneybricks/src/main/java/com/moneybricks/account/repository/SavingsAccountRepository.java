package com.moneybricks.account.repository;

import com.moneybricks.account.domain.SavingsAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT sa FROM SavingsAccount sa WHERE sa.member.username = :username")
    Optional<SavingsAccount> findByUsername(@Param("username") String username);
}
