package org.example.domain.review.strategy;

import org.example.domain.review.Review;
import java.util.List;

public interface ReviewInitStrategy {
    List<Review> initializeList();
}
