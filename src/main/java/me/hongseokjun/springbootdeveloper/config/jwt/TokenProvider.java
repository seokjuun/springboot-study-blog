package me.hongseokjun.springbootdeveloper.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.hongseokjun.springbootdeveloper.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
// TokenProvider : JWT 토큰을 생성하고, 검증. 토큰을 통해 인증 정보를 추출.
public class TokenProvider {
    private final JwtProperties jwtProperties; // 필요한 설정 정보 불러옴.


    // JWT 토큰을 생성하기 위한 메서드
    public String generateToken(User user, Duration expiredAt) { // 유저정보, 토큰 만료 기간 매개변수
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user); // makeToken 메서드를 호출하여 토큰을 생성 후 , 반환
    }

    // 1. JWT 토큰 생성 메서드 : 실제로 생성, generateToken 에서 호출되며, 토큰 만료시간과 유저 정보 기반으로 JWT 구성
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder() // 빌더 객체를 생성하여 토큰의 각 부분 정의
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ : JWT ( jwt 타입 임을 설정)
                // 내용 iss : seokjuun@gmail.com(properties 파일에서 설정한 값)
                .setIssuer(jwtProperties.getIssuer()) // 발급자 설정
                .setIssuedAt(now) // 내용 iat(발급일시) : 현재 시간 (언제 발급된 토큰인지 나타냄)
                .setExpiration(expiry) // 내용 exp(만료일시) : expiry 멤버 변숫값 (토큰의 만료시간 설정)
                .setSubject(user.getEmail()) // 내용 sub(토큰제목) : 유저의 이메일 (유저 식별 가능케함)
                .claim("id", user.getId()) // 클레임 id : 유저 ID ( 클레임으로 추가하여, 토큰에서 쉽게 유저 ID를 가져올 수 있게 함)
                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact(); // JWT 토큰을 문자열로 직렬화하여 최종 토큰을 생성
    }

    // 2. JWT 토큰 유효성 검증 메서드 : 유효여부를 boolean 타입으로 변환, 프로퍼티즈 파일에 선언한 비밀값과 함께 토큰 복호화
    public boolean validToken(String token) { // 검증할 JWT 토큰을 매개변수로
        try{
            Jwts.parser() // JWT 파서 생성
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀값으로 복호화 : 서명을 검증할 수 있도록 함
                    .parseClaimsJws(token); // 전달된 토큰을 파싱하여 유효성을 확인.
            return true;
        } catch (Exception e){ // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
            return false; 
        }
    }
    
    // 3. 토큰 기반으로 인증 정보를 가져오는 메서드 : 토큰을 기반으로 Authentication 객체를 생성
    // Authentication 객체를 반환하여 스프링 시큐리티에서 인증을 처리할 수 있게 함
    public Authentication getAuthentication(String token) { // 인증 정보를 추출할 토큰 매개변수
        Claims claims = getClaims(token); // 토큰의 클레임 정보를 가져옴, 여기 들어있는 토큰 제목 sub 와 토큰 기반으로 인증 정보를 생성
        // 기본적으로  ROLE_USER 권한을 가진 인증 정보를 생성
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        // 스프링 시큐리티의 인증 토큰 객체를 생성하여 반환. 이때, UserDetails 객체와 토큰, 권한을 함께 설정.
        return new UsernamePasswordAuthenticationToken(new org.springframework.
                security.core.userdetails.User(claims.getSubject(),
                "",authorities),token, authorities); // User 는 스프링 시큐리티에서 제공하는 객체인 User 클래스
    }

    // 4. 토큰 기반으로 유저 ID를 가져오는 메서드 : 토큰을 복호화한 후 클레임 정보를 반환받고 클레임에서 id 키로 저장된 값을 가져와 반환
    public Long getUserId(String token) { // 유저 ID를 추출할 JWT 토큰
        Claims claims = getClaims(token); // 토큰의 클레임 정보를 가져옴.
        return claims.get("id", Long.class); // id 라는 클레임 값을 Long 타입으로 반환하여 유저 ID를 제공.
    }

    // JWT 토큰의 클레임을 파싱하여 Claims 객체로 반환하는 메서드.
    private Claims getClaims(String token) {
        return Jwts.parser() // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey()) // 서명을 검증하기 위해 비밀 키 설정
                .parseClaimsJws(token) // 전달된 토큰을 파싱하여 클레임 바디를 가져옴.
                .getBody();
    }
}
