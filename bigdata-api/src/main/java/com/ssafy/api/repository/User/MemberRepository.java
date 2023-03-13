package com.ssafy.api.repository.User;

import com.ssafy.api.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
     Optional<Member> findByEmail(String email);
}

