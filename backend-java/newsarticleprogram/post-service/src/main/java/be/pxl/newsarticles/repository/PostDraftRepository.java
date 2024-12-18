package be.pxl.newsarticles.repository;

import be.pxl.newsarticles.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDraftRepository extends JpaRepository<Post, Long> {
}
