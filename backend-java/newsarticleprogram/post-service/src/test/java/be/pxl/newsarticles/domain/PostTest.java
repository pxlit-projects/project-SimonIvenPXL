package be.pxl.newsarticles.domain;

import be.pxl.newsarticles.enumdata.PostStatus;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostTest {
    private Long id;
    private String title;
    private String content;
    private String author;
    private PostStatus postStatus;
    private String reviewEditor;
    private String reviewReasoning;
    private List<Long> commentIds;
    private LocalDateTime publishedDate;

    @BeforeEach
    public void setUp() {
        id = 1L;
        title = "title";
        content = "content";
        author = "author";
        postStatus = PostStatus.PENDING;
        reviewEditor = "reviewEditor";
        reviewReasoning = "reviewReasoning";
        commentIds = new ArrayList<>();
        commentIds.add(1L);
        commentIds.add(2L);
        commentIds.add(3L);
        publishedDate = LocalDateTime.of(2025, 1, 1, 0, 0);
    }

    @Test
    public void testBuilderWithValidArgumentsShouldCreateValidPost() {
        Post post = Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .status(postStatus)
                .reviewEditor(reviewEditor)
                .reviewReasoning(reviewReasoning)
                .commentIds(commentIds)
                .publishedDate(publishedDate)
                .build();

        assertEquals(id, post.getId());
        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
        assertEquals(author, post.getAuthor());
        assertEquals(postStatus, post.getStatus());
        assertEquals(reviewEditor, post.getReviewEditor());
        assertEquals(reviewReasoning, post.getReviewReasoning());
        assertEquals(commentIds, post.getCommentIds());
        assertEquals(publishedDate, post.getPublishedDate());
    }
}
