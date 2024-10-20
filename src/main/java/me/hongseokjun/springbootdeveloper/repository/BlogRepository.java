package me.hongseokjun.springbootdeveloper.repository;

import me.hongseokjun.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
    // JpaRepository 클래스를 상속받음 -> jpa 기능 사용가능
    //  기본 crud 작업을 제공하는 인터페이스 -> save(), findById(), findAll(), deleteById()
    // 페이징 및 정렬, 커스텀 쿼리, 쿼리 어노테이션, 엔티티 클래스와 연결 등....
    // 상속받을때 엔티티 Article 과 엔티티의 PK 타입 Long 을 인수로 넣음

}
