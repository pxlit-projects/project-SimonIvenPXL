package be.pxl.reviews.service;

import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;
import be.pxl.newsarticles.enumdata.PostStatus;
import be.pxl.newsarticles.exception.ResourceNotFoundException;
import be.pxl.newsarticles.exception.WrongStatusException;
import be.pxl.reviews.client.PostClient;
import be.pxl.reviews.domain.Notification;
import be.pxl.reviews.domain.Review;
import be.pxl.reviews.dto.NotificationRequest;
import be.pxl.reviews.dto.ReviewRequest;
import be.pxl.reviews.dto.ReviewResponse;
import be.pxl.reviews.enumdata.ReviewDecision;
import be.pxl.reviews.repository.ReviewRepository;
import be.pxl.reviews.service.interfaces.IReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final PostClient postClient;
    private final ModelMapper mapper;
    private final RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    @Override
    public List<ReviewResponse> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No Reviews found");
        }

        return reviews.stream().map(r -> mapper.map(r, ReviewResponse.class)).toList();
    }

    @Override
    public ReviewResponse getReviewById(long id) {
        Optional<Review> review = reviewRepository.findById(id);

        if (review.isEmpty()) {
            throw new ResourceNotFoundException("Review not found");
        }

        return ReviewResponse.builder()
                .id(review.get().getId())
                .postId(review.get().getPostId())
                .decision(review.get().getDecision())
                .reviewEditor(review.get().getReviewEditor())
                .reasoning(review.get().getReasoning())
                .reviewedDate(review.get().getReviewedDate())
                .build();
    }

    @Override
    public Review approvePost(long postId, String editor, String reasoning) {
        logger.info("Approving post...");
        PostResponse postResponse = postClient.getPostById(postId);
        if (postResponse == null) {
            logger.info("Something went wrong! Post not found");
            throw new ResourceNotFoundException("Post with ID " + postId + " not found");
        } else {
            System.out.println("PostResponse: " + postResponse.getTitle());
        }
        if (postResponse.getStatus() != PostStatus.PENDING) {
            logger.info("Something went wrong! Wrong status");
            throw new WrongStatusException("Post is not pending!");
        }

        PostRequest postRequest = PostRequest.builder()
                .title(postResponse.getTitle())
                .content(postResponse.getContent())
                .author(postResponse.getAuthor())
                .status(PostStatus.READY_TO_PUBLISH)
                .reviewEditor(editor)
                .reviewReasoning(reasoning)
                .publishedDate(LocalDateTime.now().withNano(0))
                .commentIds(postResponse.getCommentIds())
                .build();

        try {
            postClient.saveEditsToPost(postId, postRequest);
        } catch (Exception e) {
            logger.info("Something went wrong! Failed to update status!");
            throw new WrongStatusException("Failed to update post status to PUBLISHED");
        }

        Review review = Review.builder()
                .postId(postId)
                .decision(ReviewDecision.APPROVED)
                .reasoning(reasoning)
                .reviewEditor(editor)
                .reviewedDate(LocalDateTime.now().withNano(0))
                .build();

        createNotification(review);
        logger.info("Post approved!");
        return reviewRepository.save(review);
    }

    @Override
    public Review rejectPost(long postId, String editor, String reasoning) {
        logger.info("Rejecting post with ID " + postId);
        PostResponse postResponse = postClient.getPostById(postId);
        if (postResponse == null) {
            System.out.println("Not found");
            throw new ResourceNotFoundException("Post with ID " + postId + " not found");
        } else {
            System.out.println("PostResponse: " + postResponse.getTitle());
        }
        if (postResponse.getStatus() != PostStatus.PENDING) {
            throw new WrongStatusException("Post is not pending!");
        }

        PostRequest postRequest = PostRequest.builder()
                .title(postResponse.getTitle())
                .content(postResponse.getContent())
                .author(postResponse.getAuthor())
                .status(PostStatus.REJECTED)
                .reviewEditor(editor)
                .reviewReasoning(reasoning)
                .publishedDate(LocalDateTime.now().withNano(0))
                .build();

        try {
            postClient.saveEditsToPost(postId, postRequest);
        } catch (Exception e) {
            logger.info("Something went wrong! Status could not be edited");
            throw new WrongStatusException("Failed to update post status to REJECTED");
        }

        Review review = Review.builder()
                .postId(postId)
                .decision(ReviewDecision.REJECTED)
                .reasoning(reasoning)
                .reviewEditor(editor)
                .reviewedDate(LocalDateTime.now().withNano(0))
                .build();

        createNotification(review);

        logger.info("Post rejected!");

        return reviewRepository.save(review);
    }

    private void createNotification(Review review) {
        logger.info("Sending notification to notification-service...");
        StringBuilder sb = new StringBuilder();
        sb.append("Post has been reviewed. Result: ").append(review.getDecision());
        sb.append("\n");
        if (review.getDecision() == ReviewDecision.APPROVED) {
            sb.append("The post is approved!");
        }
        else {
            sb.append("The post is rejected! Please edit according to the reasons given and try again: ").append(review.getReasoning());
        }

        String message = sb.toString();

        NotificationRequest request = NotificationRequest.builder()
                .sender(review.getReviewEditor())
                .message(message)
                .build();

        sendNotification(request);
        logger.info("Notification sent!");
    }

    private void sendNotification(NotificationRequest notificationRequest) {


        Notification notification = Notification.builder()
                .message(notificationRequest.getMessage())
                .sender(notificationRequest.getSender())
                .build();
        rabbitTemplate.convertAndSend("NotificationQueue", notification);
    }

}
