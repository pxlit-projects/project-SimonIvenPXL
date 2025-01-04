package be.pxl.newsarticles.client;

import be.pxl.services.domain.Comment;
import be.pxl.services.dto.CommentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "comment-service")
public interface CommentClient {
    @PostMapping("/api/comments")
    Comment createComment(@RequestBody CommentRequest commentRequest);
}
