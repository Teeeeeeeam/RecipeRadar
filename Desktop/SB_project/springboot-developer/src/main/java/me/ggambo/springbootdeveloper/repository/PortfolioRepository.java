package me.ggambo.springbootdeveloper.repository;

import me.ggambo.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Article, Long> {
    // 상속 받을 때 엔티티 Article과 엔티티의 PK타입 Long을 인수로 넣는다.
}
