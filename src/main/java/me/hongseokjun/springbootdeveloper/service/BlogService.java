package me.hongseokjun.springbootdeveloper.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.hongseokjun.springbootdeveloper.domain.Article;
import me.hongseokjun.springbootdeveloper.dto.AddArticleRequest;
import me.hongseokjun.springbootdeveloper.dto.UpdateArticleRequest;
import me.hongseokjun.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // final 이 붙거나 @NotNull 이 붙은 필드의 생성자 추가
@Service //BlogService 클래스가 빈으로 컨테이너에 등록, 다른 클래스에서 주입받아 사용가능해짐
public class BlogService {
    private final BlogRepository blogRepository;

    //블로그 글 추가 메서드
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity()); //JpaRepository 에서 지원하는 저장 메서드
        // AddArticleRequest 클래스에 저장된 값들을 article 데이터베이스에 저장
        // 그니까 뭔소리냐면
        // BlogService 의 save 메서드가 호출되면, 매개변수로 전달된
        // AddArticleRequest 객체를 통해 Article 객체를 생성하고,
        // 이를 blogRepository 를 통해 데이터베이스에 저장한다는거임 ㅇㅇ

        // q) 근데 저장되고 저장된 값이 반환되다는건가?
        // a) 반환됨.. 여기서 반환된건 새로운 블로그 글 정보겠지?ㅎ
    }

    // 블로그 글 목록 가져오기
    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    // 블로그 글 조회 : ID 이용
    public Article findById(long id){
        return blogRepository.findById(id) //Optional<Article> 타입으로 반환, 객체가 존재할 수 도 있고, 없을 수도 있어서
                .orElseThrow(()-> new IllegalArgumentException("not found: "+ id));
        //orElseThrow() : Optional 객체가 값을 가지고 있지 않을 때, 예외를 던지는 메서드
        // 값이 없을 때 (잘못된인자) 던질 예외를 람다로 정의
    }

    // 블로그 글 삭제 : ID를 받은 뒤, deleteByID() 메서드로 디비에서 데이터 삭제
    public void delete(long id){
        blogRepository.deleteById(id);
    }

    // 블로그 글 수정
    @Transactional //트랜잭션 메서드
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: "+ id));

        article.update(request.getTitle(), request.getContent());
        return article;
    }
}
