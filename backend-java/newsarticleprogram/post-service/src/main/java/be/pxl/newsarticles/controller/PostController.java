package be.pxl.newsarticles.controller;

import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/createPost")
    public ResponseEntity<Void> createPost(@RequestBody Post post) {
        return null;
    }
}
