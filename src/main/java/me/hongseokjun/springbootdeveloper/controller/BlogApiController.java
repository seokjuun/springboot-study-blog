package me.hongseokjun.springbootdeveloper.controller;


import lombok.RequiredArgsConstructor;
import me.hongseokjun.springbootdeveloper.domain.Article;
import me.hongseokjun.springbootdeveloper.dto.AddArticleRequest;
import me.hongseokjun.springbootdeveloper.dto.ArticleResponse;
import me.hongseokjun.springbootdeveloper.dto.UpdateArticleRequest;
import me.hongseokjun.springbootdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor // final 필드나 @NotNull 이 붙은 필드들을 포함한 생성자를 자동 생성
@RestController // HTTP Response Body 에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {
    private final BlogService blogService;

    // post 요청이 오면 @PostMappling 을 이용해 요청을 매핑한 뒤,
    // 블로그 글을 생성하는 BlogService save()메서드를 호출한 뒤,
    // 생성된 블로그 글을 반환하는 작업을 할 addArticle() 메서드 작성
    @PostMapping("/api/articles") //HTTP 메서드가 POST 일 때 전달받은 URL 과 동일하면 메서드(addArticle)로 매핑
    // @RequestBody 로 요청 본문 값 매핑
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);
        //요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }
    // @RequestBody 애너테이션은 HTTP 를 요청할 때 응답에 해당하는 값을
    // @RequestBody 가 붙은 대상 객체인 AddArticleRequest 에 매핑
    // ResponseEntity.status().body()는 응답 코드로 201, 즉, Created 를 응답하고 테이블에 저장된 객체를 반환


    //블로그 글 목록 조회
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
        // GET 요청이 오면 findAll() 메서드를 호출 후 응답용 객체인 ArticleResponse 로 파싱해 body 에 담아 클라이언트에 전송
        // ResponseEntity 는 HTTP 응답을 상태 코드, 헤더, 본문(body)에 담아 반환
    }


    //블로그 글 조회
    @GetMapping("/api/articles/{id}") // URL 에서 {id}에 해당하는 값이 id 로 들어옴
    //url 경로에서 값 추출
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id)
    {
        Article article = blogService.findById(id); // 엔티티 조회, 없으면 예외

        return ResponseEntity.ok().body(new ArticleResponse(article));
    }
    // @PathVariable : URL 에서 값을 가져오는 애너테이션, 여기선 id 값

    // 글 삭제
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id){
        blogService.delete(id);

        return ResponseEntity.ok().build(); //Http 응답 생성, build()는 본문이 없는 응답 생성
    }

    // 글 수정
    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request){
        Article updateArticle = blogService.update(id, request);
        return ResponseEntity.ok().body(updateArticle); //응답 값은 body 에 담아 전송
    }
}
