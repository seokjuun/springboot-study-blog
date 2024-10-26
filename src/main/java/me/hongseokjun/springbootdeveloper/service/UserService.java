package me.hongseokjun.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.hongseokjun.springbootdeveloper.domain.User;
import me.hongseokjun.springbootdeveloper.dto.AddUserRequest;
import me.hongseokjun.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto){
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                // 패스워드 암호화 : 시큐리티 설정하여 패스워드 인코딩용으로 등록한 빈을 사용해서 암호화한 후에 저장
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }
}
