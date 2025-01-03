package be.pxl.reviews.service;

import be.pxl.reviews.client.PostClient;
import be.pxl.reviews.dto.ReviewResponse;
import be.pxl.reviews.repository.ReviewRepository;
import be.pxl.reviews.service.interfaces.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final PostClient postClient;

    @Override
    public List<ReviewResponse> getAllReviews() {
        return List.of();
    }

    @Override
    public ReviewResponse getReviewById(int id) {
        return null;
    }
}
