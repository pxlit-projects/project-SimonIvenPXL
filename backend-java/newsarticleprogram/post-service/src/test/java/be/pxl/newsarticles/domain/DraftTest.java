package be.pxl.newsarticles.domain;

import be.pxl.newsarticles.enumdata.PostStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DraftTest {
    private Long id;
    private String title;
    private String content;
    private String author;
    private PostStatus postStatus;
    private LocalDateTime savedDate;

    @BeforeEach
    public void setUp() {
        id = 1L;
        title = "title";
        content = "content";
        author = "author";
        postStatus = PostStatus.PENDING;
        savedDate = LocalDateTime.of(2025, 1, 1, 0, 0);
    }

    @Test
    public void testBuilderWithValidArgumentsShouldCreateValidPost() {
        Draft draft = Draft.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .status(postStatus)
                .savedDate(savedDate)
                .build();

        assertEquals(id, draft.getId());
        assertEquals(title, draft.getTitle());
        assertEquals(content, draft.getContent());
        assertEquals(author, draft.getAuthor());
        assertEquals(postStatus, draft.getStatus());
        assertEquals(savedDate, draft.getSavedDate());
    }
}
