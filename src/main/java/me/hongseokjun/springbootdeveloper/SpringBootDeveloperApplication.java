package me.hongseokjun.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// 밑 애너테이션이 스프링 부트 사용에 필요한 기본 설정을 해줌
@SpringBootApplication
public class SpringBootDeveloperApplication {
    public static void main(String[] args) {
        // 애플리케이션 실행
        SpringApplication.run(SpringBootDeveloperApplication.class);
    }
}
