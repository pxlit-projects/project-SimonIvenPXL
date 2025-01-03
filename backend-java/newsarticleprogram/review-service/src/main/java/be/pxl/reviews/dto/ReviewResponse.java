package be.pxl.reviews.dto;

import be.pxl.reviews.enumdata.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long postId;
    private String reviewEditor;
    private String reasoning;
    private ReviewStatus status;
    private LocalDateTime reviewedDate;
}
