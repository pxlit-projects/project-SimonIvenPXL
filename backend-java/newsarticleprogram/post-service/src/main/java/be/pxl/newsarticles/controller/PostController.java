package be.pxl.newsarticles.controller;

import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;
import be.pxl.newsarticles.exception.ResourceNotFoundException;
import be.pxl.newsarticles.service.PostService;
import be.pxl.services.dto.CommentRequest;
import be.pxl.services.dto.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
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

    @PostMapping(path = "/{id}/comments")
    public ResponseEntity<Post> addCommentToPost(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        try {
            return new ResponseEntity<>(postService.addCommentToPost(id, commentRequest.getAuthor(), commentRequest.getContent()), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsFromPost(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(postService.getCommentsForPost(id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Post> updateCommentForPost(@PathVariable Long id, @PathVariable Long commentId, @RequestBody String comment) {
        try {
            return new ResponseEntity<>(postService.updateCommentForPost(id, commentId, comment), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Void> deleteCommentFromPost(@PathVariable Long id, @PathVariable Long commentId) {
        try {
            postService.deleteCommentById(id, commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
