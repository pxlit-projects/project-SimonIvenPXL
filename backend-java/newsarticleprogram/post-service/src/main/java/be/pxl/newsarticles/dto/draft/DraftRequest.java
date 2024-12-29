package be.pxl.newsarticles.dto.draft;

import be.pxl.newsarticles.enumdata.PostStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DraftRequest {
    private String title;

    private String content;

    private String author;

    @NotBlank(message = "Post must have a status!")
    private PostStatus status;

    @FutureOrPresent
    private LocalDateTime savedDate;
}
