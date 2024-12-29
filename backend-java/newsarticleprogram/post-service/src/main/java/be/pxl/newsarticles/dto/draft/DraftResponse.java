package be.pxl.newsarticles.dto.draft;

import be.pxl.newsarticles.enumdata.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DraftResponse {
    private String title;
    private String content;
    private String author;
    private PostStatus status;
    private LocalDateTime savedDate;
}
