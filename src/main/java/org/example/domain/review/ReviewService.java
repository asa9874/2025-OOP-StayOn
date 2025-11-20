package org.example.domain.review;

import org.example.domain.review.dto.ReviewRequestDTO;
import org.example.domain.user.customer.Customer;

import java.util.List;
import java.util.NoSuchElementException;

public class ReviewService {
    private static ReviewService instance;
    private ReviewRepository reviewRepository;

    private ReviewService() {
    }

    public static ReviewService getInstance() {
        if (instance == null) {
            instance = new ReviewService();
        }
        return instance;
    }

    public void setReviewRepository(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review save(ReviewRequestDTO requestDTO) {
        Review newReview = new Review(
                requestDTO.rate(),
                requestDTO.content(),
                requestDTO.date(),
                requestDTO.customer(),
                requestDTO.room()
        );
        return reviewRepository.save(newReview);
    }

    public Review findById(int id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 리뷰를 찾을 수 없습니다: " + id));
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<Review> findByCustomer(Customer customer) {
        return reviewRepository.findByCustomer(customer);
    }

    public List<Review> findByRate(int rate) {
        return reviewRepository.findByRate(rate);
    }

    public List<Review> findByRateGreaterThanOrEqual(int rate) {
        return reviewRepository.findByRateGreaterThanOrEqual(rate);
    }

    public List<Review> findByRoomId(int roomId) {
        return reviewRepository.findByRoomId(roomId);
    }

    public List<Review> findByRoomIdSortedByDate(int roomId) {
        return reviewRepository.findByRoomIdSortedByDate(roomId);
    }

    public void deleteById(int id) {
        Review review = findById(id);
        reviewRepository.deleteById(review.getId());
    }

    public Review updateReview(int id, int rate, String content) {
        Review review = findById(id);
        review.updateReview(rate, content);
        return review;
    }

    public double getAverageRate() {
        return reviewRepository.getAverageRate();
    }
}
