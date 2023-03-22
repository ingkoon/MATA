package com.ssafy.api.repository.mysql;

import com.ssafy.api.entity.mysql.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
     Optional<Member> findByEmail(String email);
     boolean existsByEmail(String email);

}

