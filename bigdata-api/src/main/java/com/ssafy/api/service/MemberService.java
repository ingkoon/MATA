package com.ssafy.api.service;

import com.ssafy.api.common.token.JwtTokenProvider;
import com.ssafy.api.dto.member.exception.DuplicateMemberException;
import com.ssafy.api.dto.member.exception.NoSuchMemberException;
import com.ssafy.api.dto.member.request.MemberLoginRequest;
import com.ssafy.api.dto.member.request.MemberSignUpRequest;
import com.ssafy.api.dto.member.response.MemberResponse;
import com.ssafy.api.entity.mysql.Member;
import com.ssafy.api.repository.mysql.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate stringRedisTemplate;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(MemberSignUpRequest request){
        isExistEmail(request.getEmail());
        String password = passwordEncoder.encode(request.getPassword());
        Member member = request.toEntity(password);
        memberRepository.save(member);
    }

    public MemberResponse login(MemberLoginRequest request){

        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(NoSuchMemberException::new);
        log.info(member.getEmail());

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();

        log.info("============ access authentication Token ==============");

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("============ access authentication ==============");

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        MemberResponse tokenInfo = jwtTokenProvider.generateToken(authentication, member, true, "");
        log.info("============ access token ==============");

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        stringRedisTemplate.opsForValue()
                .set("RT:" + member.getEmail(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        return tokenInfo;
    }

    public void isExistEmail(String email){
        if(memberRepository.existsByEmail(email)) throw new DuplicateMemberException();
    }

    public void logout(String accessToken)
    {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        log.info(authentication.getName());
        if(stringRedisTemplate.opsForValue().get("RT:"+authentication.getName()) !=null){
            stringRedisTemplate.delete("RT:"+authentication.getName());
        }
        else log.info("refresh 없음");
    }
}
