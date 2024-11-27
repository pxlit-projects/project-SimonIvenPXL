package be.pxl.newsarticles.dto.post;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostRequest {
    @NotBlank(message = "Title can't be empty!")
    private String title;

    @NotBlank(message = "Content can't be empty!")
    private String content;

    @NotBlank(message = "Author must be someone!")
    private String author;

    @FutureOrPresent
    @NotBlank(message = "Date of creation must be present!")
    private LocalDateTime publishedDate;
}
