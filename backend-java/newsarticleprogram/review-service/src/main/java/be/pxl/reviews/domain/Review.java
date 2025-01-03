package be.pxl.reviews.domain;

import be.pxl.reviews.enumdata.ReviewDecision;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;

    private String reviewEditor;
    private String reasoning;

    @Enumerated(EnumType.STRING)
    private ReviewDecision decision;

    private LocalDateTime reviewedDate;
}
