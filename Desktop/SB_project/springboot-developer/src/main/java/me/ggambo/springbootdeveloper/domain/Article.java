package me.ggambo.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // 엔티티로 지정
@Getter // 생성자 메서드 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 접근제어자가 protected인 기본 생성자 생성
public class Article {

    @Id // id필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 자동으로 1증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false) // title 컬럼과 매핑
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder //빌더 패턴으로 객체 생성
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
