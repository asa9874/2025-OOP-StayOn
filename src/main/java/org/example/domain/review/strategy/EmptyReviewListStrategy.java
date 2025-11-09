package org.example.domain.review.strategy;

import org.example.domain.review.Review;
import java.util.ArrayList;
import java.util.List;

public class EmptyReviewListStrategy implements ReviewInitStrategy {
    @Override
    public List<Review> initializeList() {
        return new ArrayList<>();
    }
}
