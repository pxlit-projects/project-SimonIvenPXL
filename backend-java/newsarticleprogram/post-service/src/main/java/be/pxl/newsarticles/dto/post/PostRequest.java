package be.pxl.newsarticles.dto.post;


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
public class PostRequest {
    @NotBlank(message = "Title can't be empty!")
    private String title;

    @NotBlank(message = "Content can't be empty!")
    private String content;

    @NotBlank(message = "Author must be someone!")
    private String author;

    @NotBlank(message = "Post must have a status!")
    private PostStatus status;

    @FutureOrPresent
    private LocalDateTime publishedDate;
}
