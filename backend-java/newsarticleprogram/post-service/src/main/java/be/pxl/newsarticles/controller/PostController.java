package be.pxl.newsarticles.controller;

import be.pxl.newsarticles.domain.Draft;
import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.draft.DraftRequest;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {
    private final PostService postService;

    @PostMapping()
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest) {
        return new ResponseEntity<>(postService.createPost(postRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts() {
        try {
            return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Post> saveEditsToPost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        try {
            return new ResponseEntity<>(postService.saveEditsToPost(id, postRequest), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
