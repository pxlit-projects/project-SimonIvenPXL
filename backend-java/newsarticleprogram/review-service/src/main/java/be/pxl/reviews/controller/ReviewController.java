package be.pxl.reviews.controller;

import be.pxl.newsarticles.exception.ResourceNotFoundException;
import be.pxl.newsarticles.exception.WrongStatusException;
import be.pxl.reviews.domain.Review;
import be.pxl.reviews.dto.ReviewRequest;
import be.pxl.reviews.dto.ReviewResponse;
import be.pxl.reviews.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        try {
            return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long reviewId) {
        try {
            return new ResponseEntity<>(reviewService.getReviewById(reviewId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{postId}/approve")
    public ResponseEntity<Review> approvePost(@PathVariable Long postId, @RequestBody ReviewRequest reviewRequest) {
        try {
            return new ResponseEntity<>(reviewService.approvePost(postId, reviewRequest.getReviewEditor(), reviewRequest.getReasoning()), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (WrongStatusException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{postId}/reject")
    public ResponseEntity<Review> rejectPost(@PathVariable Long postId, @RequestBody ReviewRequest reviewRequest) {
        try {
            return new ResponseEntity<>(reviewService.rejectPost(postId, reviewRequest.getReviewEditor(), reviewRequest.getReasoning()), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (WrongStatusException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
