package be.pxl.services.controller;

import be.pxl.services.domain.Comment;
import be.pxl.services.dto.CommentRequest;
import be.pxl.services.dto.CommentResponse;
import be.pxl.services.exception.ResourceNotFoundException;
import be.pxl.services.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequest commentRequest) {
        return new ResponseEntity<>(commentService.createComment(commentRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments() {
        try {
            return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(commentService.getCommentById(id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
