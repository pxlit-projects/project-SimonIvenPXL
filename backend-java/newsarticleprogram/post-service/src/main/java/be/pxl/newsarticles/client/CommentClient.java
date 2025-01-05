package be.pxl.newsarticles.client;

import be.pxl.services.domain.Comment;
import be.pxl.services.dto.CommentRequest;
import be.pxl.services.dto.CommentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "comment-service")
public interface CommentClient {
    @PostMapping("/api/comments")
    Comment createComment(@RequestBody CommentRequest commentRequest);

    @GetMapping("/api/comments/{id}")
    CommentResponse getCommentById(@PathVariable Long id);
}
