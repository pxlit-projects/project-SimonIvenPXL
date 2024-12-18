package be.pxl.newsarticles.controller;

import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;
import be.pxl.newsarticles.exception.ResourceNotFoundException;
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
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest) {
        return new ResponseEntity<>(postService.createPost(postRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Post> savePostAsDraft(@RequestBody PostRequest postRequest) {
        try {
            return new ResponseEntity<>(postService.savePostAsDraft(postRequest), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
