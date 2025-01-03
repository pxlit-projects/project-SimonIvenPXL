package be.pxl.reviews.service.interfaces;

import be.pxl.reviews.domain.Review;
import be.pxl.reviews.dto.ReviewRequest;
import be.pxl.reviews.dto.ReviewResponse;

import java.util.List;

public interface IReviewService {
    List<ReviewResponse> getAllReviews();
    ReviewResponse getReviewById(long id);
    Review approvePost(long postId, String editor, String reasoning);
    Review rejectPost(long postId, String editor, String reasoning);
}
