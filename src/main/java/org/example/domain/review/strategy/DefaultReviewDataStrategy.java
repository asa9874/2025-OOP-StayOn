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
        Customer[] customers = new Customer[25];
        String[] names = {"김철수", "이영희", "박민수", "최수지", "정준호",
                         "강지은", "조성민", "윤하늘", "장서연", "임도윤",
                         "한예준", "오시우", "서하준", "신서준", "권지호",
                         "황수빈", "안은우", "송유진", "류지우", "전현우",
                         "홍민지", "고태양", "문소율", "양채원", "손다은"};
        
        for (int i = 0; i < 25; i++) {
            customers[i] = new Customer(
                names[i],
                "customer" + (i + 1),
                "password" + (i + 1),
                "010-" + (1000 + i * 100) + "-" + (2000 + i * 100),
                "customer" + (i + 1) + "@example.com",
                100000
            );
            customers[i].setId(i + 1);
        }

        // Review 생성
        String[] comments = {
            "정말 좋은 펜션이었습니다! 깨끗하고 조용해요.",
            "전반적으로 만족스러웠습니다. 다음에 또 방문하고 싶어요.",
            "가족과 함께 좋은 시간을 보냈습니다. 추천합니다!",
            "경치가 아름답고 시설이 깨끗했어요.",
            "직원분들이 친절하고 서비스가 훌륭했습니다.",
            "조용하고 편안한 휴식을 취할 수 있었어요.",
            "가격 대비 만족스러운 숙소였습니다.",
            "주변 관광지도 가깝고 위치가 좋아요.",
            "취사를 할 수 있는 시설이 있어서 편리했어요.",
            "방이 넓고 깨끗해서 좋았습니다.",
            "아이들과 함께 즐겁게 보내기 좋은 곳이에요.",
            "주차 공간이 넓어서 편했습니다.",
            "조식이 맛있고 양도 충분했어요.",
            "밤 경치가 정말 환상적이었습니다.",
            "친구들과 함께 즉기기 좋은 숙소였어요.",
            "내부 인테리어가 세련되고 아늦했어요.",
            "주변이 조용하고 공기가 맑았습니다.",
            "BBQ 시설이 있어서 즐거운 시간을 보냈어요.",
            "수영장이 깨끗하고 물도 맑았습니다.",
            "체크인/체크아웃이 간편하고 빠르게 진행되었어요.",
            "반려동물과 함께 묵을 수 있어서 좋았습니다.",
            "외부 테라스가 넘나서 말차를 마시기 좋았어요.",
            "오션뷰가 정말 환상적이고 힐링이 되었어요.",
            "침대가 편안하고 침구가 깨끗했습니다.",
            "다시 방문하고 싶은 장소입니다. 강력 추천!"
        };
        
        int[] rates = {5, 4, 5, 4, 5, 3, 4, 5, 4, 5, 3, 4, 5, 5, 4, 5, 4, 5, 4, 3, 5, 4, 5, 5, 5};
        
        for (int i = 1; i <= 25; i++) {
            Review review = new Review();
            review.setId(i);
            review.setRate(rates[i - 1]);
            review.setContent(comments[i - 1]);
            review.setDate(LocalDate.of(2025, (i % 12) + 1, (i % 28) + 1));
            review.setCustomer(customers[i - 1]);
            list.add(review);
        }

        return list;
    }
}
