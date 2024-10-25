package me.hongseokjun.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.hongseokjun.springbootdeveloper.domain.User;
import me.hongseokjun.springbootdeveloper.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor // final 로 선언된 필드에 대한 생성자 자동 생성
@Service // 서비스 레이어, 스프링 빈으로 등록
//스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository; // 사용자 정보를 데이터베이스에서 조회하는 역할

    //사용자 이름 (email)로 사용자의 정보를 가져오는 메서드
    @Override
    public User loadUserByUsername(String email){
        return userRepository.findByEmail(email) // 반환시 Optional<Users>로
                .orElseThrow(() -> new IllegalArgumentException(email));
    } // UserDetailService 인터페이스에서 제공하는 메서드를 구현한것, 사용자 이름(email)을 통해 사용자의 인증 정보를 불러옴
}
