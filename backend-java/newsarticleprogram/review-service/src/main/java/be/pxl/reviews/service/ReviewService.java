package be.pxl.reviews.service;

import be.pxl.reviews.client.PostClient;
import be.pxl.reviews.repository.ReviewRepository;
import be.pxl.reviews.service.interfaces.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final PostClient postClient;
}
