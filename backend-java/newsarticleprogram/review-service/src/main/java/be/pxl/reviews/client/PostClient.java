package be.pxl.reviews.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post-service")
public interface PostClient {
    @GetMapping("/api/posts/{id}")
    void getPostById(@PathVariable Long id);
}
