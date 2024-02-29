package me.ggambo.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ggambo.springbootdeveloper.domain.Article;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {
// 단순히 데이터를 옮기기 위해 사용하는 전달자 역할로 비즈니스 로직이 없다.
    private String title;
    private String content;

    public Article toEntity() { // 생성자를 사용해 객체 생성
        // 글 추가시 저장할 엔티티로 변환하는 용도
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
