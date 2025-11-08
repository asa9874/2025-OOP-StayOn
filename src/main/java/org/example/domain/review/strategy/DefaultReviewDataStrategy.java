package org.example.domain.review.strategy;

import org.example.domain.review.Review;
import org.example.domain.user.customer.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DefaultReviewDataStrategy implements ReviewInitStrategy {
    @Override
    public List<Review> initializeList() {
        List<Review> list = new ArrayList<>();

        // 테스트용 Customer 생성
        Customer customer1 = new Customer("김철수", "customer1", "password1", "010-1111-2222", "kim@example.com", 100000);
        customer1.setId(1);

        Customer customer2 = new Customer("이영희", "customer2", "password2", "010-3333-4444", "lee@example.com", 150000);
        customer2.setId(2);

        // Review 생성
        Review review1 = new Review();
        review1.setId(1);
        review1.setRate(5);
        review1.setContent("정말 좋은 펜션이었습니다! 깨끗하고 조용해요.");
        review1.setDate(LocalDate.of(2025, 1, 15));
        review1.setCustomer(customer1);

        Review review2 = new Review();
        review2.setId(2);
        review2.setRate(4);
        review2.setContent("전반적으로 만족스러웠습니다. 다음에 또 방문하고 싶어요.");
        review2.setDate(LocalDate.of(2025, 2, 20));
        review2.setCustomer(customer2);

        Review review3 = new Review();
        review3.setId(3);
        review3.setRate(5);
        review3.setContent("가족과 함께 좋은 시간을 보냈습니다. 추천합니다!");
        review3.setDate(LocalDate.of(2025, 3, 10));
        review3.setCustomer(customer1);

        list.add(review1);
        list.add(review2);
        list.add(review3);

        return list;
    }
}
