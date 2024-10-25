package me.hongseokjun.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hongseokjun.springbootdeveloper.domain.Article;
import me.hongseokjun.springbootdeveloper.dto.AddArticleRequest;
import me.hongseokjun.springbootdeveloper.dto.ArticleResponse;
import me.hongseokjun.springbootdeveloper.dto.UpdateArticleRequest;
import me.hongseokjun.springbootdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest // 테스트용 애플리케이션 컨텍스트
@AutoConfigureMockMvc // MockMvc 생성 및 자동 구성, 컨트롤러 테스트를 위한 MockMVC 객체 자동 구성
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc; //HTTP 요청과 응답을 모의로 처리할 수 있는 객체, 서버 실행 않고 컨트롤러에 요청, 응답 가능

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화(자바 객체를 json 데이터로 변환), 역직렬화(반대)를 위한 클래스

    @Autowired
    private WebApplicationContext context; // 현재의 Spring 애플리케이션 컨텍스트를 담고 있는 객체
    // MockMvcBuilders.webAppContextSetup(context)로 웹 애플리케이션 컨텍스트를 기반으로 MockMvc를 설정할 수 있게 해줌
    // 애플리케이션 컨텍스트 : 애플리케이션을 실행하는 동안의 환경을 관리하는 객체, 빈 컨테이너 역할
    //              빈들을 관리하고, 생명 주기, 의존성 주입등을 처리

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach // 테스트 실핼 전 실행하는 메서드
    public void mockMvcSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        // 웹 애플리케이션 컨텍스트를 기반으로 MockMvc 객체 생성, 이를 통해 서버 실행않고 테스트 가능
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
        // 객체 JSON 으로 직렬화 -> writeValueAsString() 메서드로
        final String requestBody = objectMapper.writeValueAsString(userRequest);
                // title, content 로 dto 객체 만들고 json 으로 직렬화 시킴

        // when
        //설정한 내용을 바탕으로 요청 전송
        // MockMvc 를 사용해 HTTP 메서드, URL, 요청 본문, 요청 타입 등을 설정 후, 테스트 요청 보냄
        // ResultActions : 테스트에서 HTTP 요청의 결과를 다루기 위한 객체, MockMvc.perform()의 결과로 반환됨
        ResultActions result = mockMvc.perform(post(url) // perform() : HTTP 요청을 모의로 수행 , post(url) : POST 요청 지정 및 글 추가할 엔드포인트 의미
                .contentType(MediaType.APPLICATION_JSON_VALUE) // 요청 시, 타입 골라 요청보냄 여기선 제이슨
                .content(requestBody)); // JSON 데이터가 서버로 전송

        // then : 검증 단계
        result.andExpect(status().isCreated()); // result : 실행결과, andExpect(...) : 201 상태 코드가 반환되는지 검증
        List<Article> articles = blogRepository.findAll(); // DB에 저장된 모든 블로그 글 객체를 담은 리스트 생성

        assertThat(articles.size()).isEqualTo(1); // 크기가 1인지 검증 -> 블로그 글 크기가 1이어야 함
        assertThat(articles.get(0).getTitle()).isEqualTo(title); // 첫 글의 제목 내용이 각각 일치하는지 확인
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception {
        //given : 블로그 글을 저장
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content).build());
        //when : 목록 조회 API 를 호출
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON)); // 서버가 응답할 때 json 형식으로 응답을 받겠다.
        //then : 응답코드가 200 ok 이고, 반환받은 값 중에 0번째 요소의 content 와 title 이 저장된 값과 같은지 확인
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
        // andExpect 는 MockMvc 와 함께 HTTP 응답을 검증하는데 특화된 메서드
        // MockMvc 가 실제로 컨트롤러에 요청을 보내고, 응답을 검증하는 과정에서 필요한 메서드
    }


    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        //given : 블로그 글을 저장
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        //when : 저장한 블로그 글의 id 값으로 호출
        final ResultActions resultActions = mockMvc.perform(get(url,savedArticle.getId()));
        // perform() : 실제로 HTTP 요청을 전송하는 역할
        // url 에서 {id}는 savedArticle.getID()로 대체됨.

        //then : 응답코드가 200 ok 이고, 반환받은 content title
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }


    @DisplayName("deleteArticle : 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        //given : 블로그 글을 저장
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        //when : 저장한 블로그 글의 id 값으로 삭제 api 호출
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());
        //then : 응답코드 200, 블로그 글 리스트를 전체 조회해 배열 크기 0인지 확인
        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle : 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {
        //given : 블로그 글을 저장하고, 블로그 글 수정에 필요한 요청 객체를 만듬
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build()); // 저장된 글

        final String newTitle = "newTitle";
        final String newContent = "newContent";
        // 수정 될 내용 DTO 에 담아 생성
        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // when : UPDATE API 로 수정 요청 보냄, 요청 타입 제이슨, 미리 만든 객체를 요청 본문으로 전송
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE) // "이 요청은 JSON 형식으로 된 데이터 포함하고 있어요~"
                .content(objectMapper.writeValueAsString(request))); //수정 요청 DTO 를 JSON 형식으로 변환 후 담기
                // objectMapper : 자바 객체를 json 으로 직렬화
                // content : HTTP 요청의 본문을 의미
        // then : 응답코드 200 인지 확인, 블로그 글 id로 조회한 후 값이 수정되었는지 확인
        result.andExpect(status().isOk());
        Article article = blogRepository.findById(savedArticle.getId()).get();
        //findById() 는 Optional<Article>을 반환함. (값이 없을때 null 반환하기 위해)
        // get()은 해당 Optional 에서 실제 값을 꺼내오는 메서드
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);

    }
}