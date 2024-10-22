package me.hongseokjun.springbootdeveloper.controller;


import lombok.RequiredArgsConstructor;
import me.hongseokjun.springbootdeveloper.domain.Article;
import me.hongseokjun.springbootdeveloper.dto.ArticleListViewResponse;
import me.hongseokjun.springbootdeveloper.dto.ArticleViewResponse;
import me.hongseokjun.springbootdeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {
    private final BlogService blogService;

    // 블로그 글 전체 리스트를 담은 뷰를 반환
    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new).toList();
        model.addAttribute("articles",articles); // "articles" 키에 블로그 글 리스트 저장

        return "articleList"; //articleList.html 을 찾도록 함
    }

    // 블르그 글을 반환할 컨틀롤러
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    // 수정 화면 보여주기 위한 컨트롤러
    @GetMapping("/new-article")
    // 1. id 키를 가진 쿼리 파라미터의 값을 id 변수에 매핑 (id 는 없을 수도 있음)
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if(id == null){ // 2. id가 없으면 생성 : 기본 생성자를 이용해 빈 ArticleViewResponse 객체를 만듬
            model.addAttribute("article", new ArticleViewResponse());
        } else { // 3. id가 있으면 수정
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
