package be.pxl.newsarticles.controller;

import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;
import be.pxl.newsarticles.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping()
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        postService.createPost(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }
}
