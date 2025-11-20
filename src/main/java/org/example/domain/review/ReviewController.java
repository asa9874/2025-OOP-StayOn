package org.example.domain.review;

import org.example.domain.review.dto.ReviewRequestDTO;
import org.example.domain.user.customer.Customer;

import java.util.List;

public class ReviewController {
    private static ReviewController instance;
    private ReviewService reviewService;

    private ReviewController() {
    }

    public static ReviewController getInstance() {
        if (instance == null) {
            instance = new ReviewController();
        }
        return instance;
    }

    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public Review save(ReviewRequestDTO requestDTO) {
        return reviewService.save(requestDTO);
    }

    public Review findById(int id) {
        return reviewService.findById(id);
    }

    public List<Review> findAll() {
        return reviewService.findAll();
    }

    public List<Review> findByCustomer(Customer customer) {
        return reviewService.findByCustomer(customer);
    }

    public List<Review> findByRate(int rate) {
        return reviewService.findByRate(rate);
    }

    public List<Review> findByRateGreaterThanOrEqual(int rate) {
        return reviewService.findByRateGreaterThanOrEqual(rate);
    }

    public List<Review> findByRoomId(int roomId) {
        return reviewService.findByRoomId(roomId);
    }

    public List<Review> findByRoomIdSortedByDate(int roomId) {
        return reviewService.findByRoomIdSortedByDate(roomId);
    }

    public void deleteById(int id) {
        reviewService.deleteById(id);
    }

    public Review updateReview(int id, int rate, String content) {
        return reviewService.updateReview(id, rate, content);
    }

    public double getAverageRate() {
        return reviewService.getAverageRate();
    }
}
