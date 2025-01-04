package be.pxl.newsarticles.dto.post;

import be.pxl.newsarticles.enumdata.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private PostStatus status;
    private String reviewEditor;
    private String reviewReasoning;
    private LocalDateTime publishedDate;
    private List<Long> commentIds;
}
