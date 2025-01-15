package be.pxl.reviews.service;

import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.post.PostResponse;
import be.pxl.newsarticles.enumdata.PostStatus;
import be.pxl.newsarticles.exception.ResourceNotFoundException;
import be.pxl.newsarticles.exception.WrongStatusException;
import be.pxl.newsarticles.repository.PostRepository;
import be.pxl.reviews.client.PostClient;
import be.pxl.reviews.domain.Review;
import be.pxl.reviews.dto.ReviewRequest;
import be.pxl.reviews.dto.ReviewResponse;
import be.pxl.reviews.enumdata.ReviewDecision;
import be.pxl.reviews.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    private Review review1;
    private Review review2;
    private Post post;

    private ReviewResponse response1;
    private ReviewResponse response2;

    private PostResponse postResponse;

    private ReviewRequest request;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostClient client;

    @Mock
    private ModelMapper mapper;

    @Mock
    private RabbitTemplate template;

    @InjectMocks
    private ReviewService reviewService;

    @Captor
    private ArgumentCaptor<Review> reviewCaptor;

    @Captor
    private ArgumentCaptor<Post> postCaptor;

    @BeforeEach
    public void setUp() {
        review1 = Review.builder()
                .id(1L)
                .postId(1L)
                .reasoning("Reasoning 1")
                .reviewEditor("Author 1")
                .decision(ReviewDecision.APPROVED)
                .reviewedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        review2 = Review.builder()
                .id(1L)
                .postId(2L)
                .reasoning("Reasoning 2")
                .reviewEditor("Author 2")
                .decision(ReviewDecision.REJECTED)
                .reviewedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        post = Post.builder()
                .id(1L)
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .status(PostStatus.PENDING)
                .reviewEditor("Reviewer 1")
                .reviewReasoning("Reasoning 1")
                .commentIds(List.of())
                .publishedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();


        response1 = ReviewResponse.builder()
                .id(review1.getId())
                .postId(review1.getPostId())
                .reasoning(review1.getReasoning())
                .reviewEditor(review1.getReviewEditor())
                .decision(review1.getDecision())
                .reviewedDate(review1.getReviewedDate())
                .build();

        response2 = ReviewResponse.builder()
                .id(review2.getId())
                .postId(review2.getPostId())
                .reasoning(review2.getReasoning())
                .reviewEditor(review2.getReviewEditor())
                .decision(review2.getDecision())
                .reviewedDate(review2.getReviewedDate())
                .build();

        postResponse = PostResponse.builder()
                .id(1L)
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .status(PostStatus.PENDING)
                .reviewEditor("Reviewer 1")
                .reviewReasoning("Reasoning 1")
                .commentIds(List.of())
                .publishedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        request = ReviewRequest.builder()
                .reasoning("Reasoning request")
                .reviewEditor("Author request")
                .build();
    }

    @Test
    public void testGetAllPostsWithSavedPostsShouldReturnPosts() {
        Mockito.when(reviewRepository.findAll()).thenReturn(List.of(review1, review2));
        Mockito.when(mapper.map(review1, ReviewResponse.class)).thenReturn(response1);
        Mockito.when(mapper.map(review2, ReviewResponse.class)).thenReturn(response2);

        List<ReviewResponse> reviewResponses = reviewService.getAllReviews();

        Assertions.assertEquals(2, reviewResponses.size());
        Assertions.assertEquals(reviewResponses.get(0).getId(), review1.getId());
        Assertions.assertEquals(reviewResponses.get(0).getPostId(), review1.getPostId());
        Assertions.assertEquals(reviewResponses.get(0).getDecision(), review1.getDecision());
        Assertions.assertEquals(reviewResponses.get(0).getReasoning(), review1.getReasoning());
        Assertions.assertEquals(reviewResponses.get(0).getReviewEditor(), review1.getReviewEditor());
        Assertions.assertEquals(reviewResponses.get(0).getReviewedDate(), review1.getReviewedDate());

        Assertions.assertEquals(reviewResponses.get(1).getId(), review2.getId());
        Assertions.assertEquals(reviewResponses.get(1).getPostId(), review2.getPostId());
        Assertions.assertEquals(reviewResponses.get(1).getDecision(), review2.getDecision());
        Assertions.assertEquals(reviewResponses.get(1).getReasoning(), review2.getReasoning());
        Assertions.assertEquals(reviewResponses.get(1).getReviewEditor(), review2.getReviewEditor());
        Assertions.assertEquals(reviewResponses.get(1).getReviewedDate(), review2.getReviewedDate());

        Mockito.verify(reviewRepository).findAll();
    }

    @Test
    public void testGetAllDraftsWithNoneShouldThrowNotFoundException() {
        Mockito.when(reviewRepository.findAll()).thenReturn(List.of());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> reviewService.getAllReviews());

        Mockito.verify(reviewRepository).findAll();
    }

    @Test
    public void testGetDraftByIdAndValidIdShouldReturnPost() {
        Mockito.when(reviewRepository.findById(1L)).thenReturn(Optional.of(review1));

        ReviewResponse reviewResponse = reviewService.getReviewById(1L);

        Assertions.assertEquals(reviewResponse.getId(), review1.getId());
        Assertions.assertEquals(reviewResponse.getPostId(), review1.getPostId());
        Assertions.assertEquals(reviewResponse.getDecision(), review1.getDecision());
        Assertions.assertEquals(reviewResponse.getReasoning(), review1.getReasoning());
        Assertions.assertEquals(reviewResponse.getReviewEditor(), review1.getReviewEditor());
        Assertions.assertEquals(reviewResponse.getReviewedDate(), review1.getReviewedDate());

        Mockito.verify(reviewRepository).findById(1L);
    }

    @Test
    public void testGetDraftByIdAndInvalidIdShouldThrowNotFoundException() {
        Mockito.when(reviewRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> reviewService.getReviewById(3L));

        Mockito.verify(reviewRepository).findById(3L);
    }

    @Test
    public void testApprovePostWithInvalidPostIdShouldThrowNotFoundException() {
        Mockito.when(client.getPostById(3L)).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> reviewService.approvePost(3L, "test", "test"));

        Mockito.verify(client).getPostById(3L);
    }

    @Test
    public void testRejectPostWithInvalidPostIdShouldThrowNotFoundException() {
        Mockito.when(client.getPostById(3L)).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> reviewService.rejectPost(3L, "test", "test"));

        Mockito.verify(client).getPostById(3L);
    }

    @Test
    public void testPostStatusNotPendingShouldThrowWrongStatusException() {
        Mockito.when(client.getPostById(1L)).thenReturn(postResponse);

        postResponse.setStatus(PostStatus.PUBLISHED);

        Assertions.assertThrows(WrongStatusException.class, () -> reviewService.approvePost(1L, "test", "test"));
        Assertions.assertThrows(WrongStatusException.class, () -> reviewService.rejectPost(1L, "test", "test"));

    }

    @Test
    public void testApprovedReviewShouldBeCreatedWhenAllValid() {
        Mockito.when(client.getPostById(1L)).thenReturn(postResponse);
        reviewService.approvePost(1L, review1.getReviewEditor(), review1.getReasoning());
        Mockito.verify(reviewRepository).save(reviewCaptor.capture());
        Review post = reviewCaptor.getValue();

        Assertions.assertEquals(review1.getReasoning(), post.getReasoning());
        Assertions.assertEquals(review1.getPostId(), post.getPostId());
        Assertions.assertEquals(review1.getReviewEditor(), post.getReviewEditor());
        Assertions.assertEquals(ReviewDecision.APPROVED, post.getDecision());
        Assertions.assertEquals(LocalDateTime.now().withNano(0), post.getReviewedDate());
    }

    @Test
    public void testRejectedReviewShouldBeCreatedWhenAllValid() {
        Mockito.when(client.getPostById(1L)).thenReturn(postResponse);
        reviewService.rejectPost(1L, review1.getReviewEditor(), review1.getReasoning());
        Mockito.verify(reviewRepository).save(reviewCaptor.capture());
        Review post = reviewCaptor.getValue();

        Assertions.assertEquals(review1.getReasoning(), post.getReasoning());
        Assertions.assertEquals(review1.getPostId(), post.getPostId());
        Assertions.assertEquals(review1.getReviewEditor(), post.getReviewEditor());
        Assertions.assertEquals(ReviewDecision.REJECTED, post.getDecision());
        Assertions.assertEquals(LocalDateTime.now().withNano(0), post.getReviewedDate());
    }


}
