package me.hongseokjun.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users") // 이 엔티티는 users 라는 이름의 데이터베이스 테이블과 연결
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터 없는 생성자, 접근 수준 프로텍티드
@Getter
@Entity // JPA 엔티티임을 나타냄, 데이터베이스 테이블과 매핑
// UserDetails 클래스는 스프링 시큐리티에서 사용자의 인증 정보를 담아 두는 인터페이스
// 해당 객체를 통해 인증 정보를 가져오기 위해 필수 오버라이드 메서드들을 여러개 사용해야함, 인증 객체로 사용 가능
public class User implements UserDetails { // UserDetails 를 상속받아 인증 객체로 사용
    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Builder // 롬복 빌더 패턴
    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }


    // 사용자가 가지고 있는 권한의 목록을 반환. 현재 코드에서는 사용자 이외의 권한이 없기 때문에
    // user 권한만 담아 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    // 사용자의 id를 반환(고유한 값)
    // 반드시 고유한 값, 유니크 속성인 이메일 반환
    @Override
    public String getUsername(){
        return email;
    }

    // 사용자의 패스워드 반환
    // 암호화 하여 저장해야함
    @Override
    public String getPassword(){
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled(){
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }
}
