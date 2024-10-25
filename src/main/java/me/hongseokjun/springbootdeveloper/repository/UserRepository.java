package me.hongseokjun.springbootdeveloper.repository;

import me.hongseokjun.springbootdeveloper.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // email 로 사용자 정보 가져옴
    // 사용자 정보를 가져오기 위해 스프링 시큐리티가 이메일을 전달받아야 함
    // findByEmail() 메서드는 스프링 데이터 JPA 가 자동 쿼리 생성
    // findByEmail() : 실제 데이터베이스에 회원 정보를 요청할때 다음 쿼리 실행
    // FROM users
    // WHERE email = #{email}
}
