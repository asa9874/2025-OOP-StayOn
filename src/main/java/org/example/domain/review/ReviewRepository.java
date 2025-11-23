package org.example.domain.review;

import org.example.domain.review.strategy.ReviewInitStrategy;
import org.example.domain.user.customer.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewRepository {
    private static ReviewRepository instance;
    private List<Review> reviewList;
    private int nextId = 1;
    private ReviewInitStrategy initStrategy;

    private ReviewRepository() {
    }

    public static ReviewRepository getInstance() {
        if (instance == null) {
            instance = new ReviewRepository();
        }
        return instance;
    }

    public void setInitStrategy(ReviewInitStrategy initStrategy) {
        this.initStrategy = initStrategy;
        this.reviewList = initStrategy.initializeList();
        if (!reviewList.isEmpty()) {
            this.nextId = reviewList.stream()
                    .mapToInt(Review::getId)
                    .max()
                    .orElse(0) + 1;
        }
    }

    public void returnToDefaultData() {
        if (initStrategy != null) {
            this.reviewList = initStrategy.initializeList();
            if (!reviewList.isEmpty()) {
                this.nextId = reviewList.stream()
                        .mapToInt(Review::getId)
                        .max()
                        .orElse(0) + 1;
            } else {
                this.nextId = 1;
            }
        }
    }

    public Review save(Review review) {
        review.setId(nextId++);
        reviewList.add(review);
        return review;
    }

    public Optional<Review> findById(int id) {
        return reviewList.stream()
                .filter(review -> review.getId() == id)
                .findFirst();
    }

    public List<Review> findAll() {
        return new ArrayList<>(reviewList);
    }



    public List<Review> findByCustomer(Customer customer) {
        return reviewList.stream()
                .filter(review -> review.getCustomer().getId() == customer.getId())
                .toList();
    }

    public List<Review> findByRate(int rate) {
        return reviewList.stream()
                .filter(review -> review.getRate() == rate)
                .toList();
    }

    public List<Review> findByRateGreaterThanOrEqual(int rate) {
        return reviewList.stream()
                .filter(review -> review.getRate() >= rate)
                .toList();
    }

    public List<Review> findByRoomId(int roomId) {
        return reviewList.stream()
                .filter(review -> review.getRoom() != null && review.getRoom().getId() == roomId)
                .toList();
    }

    public List<Review> findByRoomIdSortedByDate(int roomId) {
        List<Review> reviews = reviewList.stream()
                .filter(review -> review.getRoom() != null && review.getRoom().getId() == roomId)
                .toList();
        return mergeSort(new ArrayList<>(reviews), 0, reviews.size() - 1);
    }

    private List<Review> mergeSort(List<Review> reviews, int left, int right) {
        if (left >= right) {
            if (left == right) {
                List<Review> result = new ArrayList<>();
                result.add(reviews.get(left));
                return result;
            }
            return new ArrayList<>();
        }

        int mid = (left + right) / 2;
        List<Review> leftSorted = mergeSort(reviews, left, mid);
        List<Review> rightSorted = mergeSort(reviews, mid + 1, right);

        return merge(leftSorted, rightSorted);
    }

    private List<Review> merge(List<Review> left, List<Review> right) {
        List<Review> merged = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            // 최신 날짜 순서 (내림차순)
            if (left.get(i).getDate().compareTo(right.get(j).getDate()) >= 0) {
                merged.add(left.get(i));
                i++;
            } else {
                merged.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
    }

    public void deleteById(int id) {
        reviewList.removeIf(review -> review.getId() == id);
    }

    public double getAverageRate() {
        if (reviewList.isEmpty()) {
            return 0.0;
        }
        return reviewList.stream()
                .mapToInt(Review::getRate)
                .average()
                .orElse(0.0);
    }
}
