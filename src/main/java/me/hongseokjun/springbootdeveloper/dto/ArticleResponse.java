package me.hongseokjun.springbootdeveloper.dto;

import lombok.Getter;
import me.hongseokjun.springbootdeveloper.domain.Article;

@Getter
public class ArticleResponse {
    private final String title;
    private final String content;
    // 엔티티를 인수로 받는 생성자
    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
