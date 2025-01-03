package be.pxl.reviews.service.interfaces;

import be.pxl.reviews.dto.ReviewResponse;

import java.util.List;

public interface IReviewService {
    List<ReviewResponse> getAllReviews();
    ReviewResponse getReviewById(int id);

}
