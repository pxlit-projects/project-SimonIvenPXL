package be.pxl.newsarticles.repository;

import be.pxl.newsarticles.domain.Draft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDraftRepository extends JpaRepository<Draft, Long> {
}
