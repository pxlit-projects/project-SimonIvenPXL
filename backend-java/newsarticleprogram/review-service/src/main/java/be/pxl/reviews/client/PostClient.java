package be.pxl.reviews.client;

import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "post-service")
public interface PostClient {
    @GetMapping("/api/posts/{id}")
    PostResponse getPostById(@PathVariable Long id);

    @PutMapping("/api/posts/{id}")
    Post saveEditsToPost(@PathVariable Long id, @RequestBody PostRequest postRequest);
}
