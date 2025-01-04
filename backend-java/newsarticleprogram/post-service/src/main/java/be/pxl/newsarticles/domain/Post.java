package be.pxl.newsarticles.domain;

import be.pxl.newsarticles.enumdata.PostStatus;
import be.pxl.services.domain.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String author;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private String reviewEditor;
    private String reviewReasoning;

    @ElementCollection
    @CollectionTable(name = "post_comments", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "comment_id")
    private List<Long> commentIds;

    @JsonProperty("publishedDate")
    private LocalDateTime publishedDate;
}
