package org.example.domain.review;

import org.example.Init;
import org.example.domain.review.dto.ReviewRequestDTO;
import org.example.domain.review.strategy.EmptyReviewListStrategy;
import org.example.domain.user.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {
    private ReviewController controller;
    private ReviewRepository repository;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        // Customer 먼저 초기화 (Review가 Customer를 참조함)
        Init.initializeCustomerModule(new org.example.domain.user.customer.strategy.DefaultCustomerDataStrategy());
        Init.initializeReviewModule(new EmptyReviewListStrategy());
        repository = ReviewRepository.getInstance();
        controller = ReviewController.getInstance();
        repository.returnToDefaultData();

        // 테스트용 Customer 생성
        testCustomer = new Customer("테스트유저", "testuser", "password", "010-1234-5678", "test@example.com", 100000);
        testCustomer.setId(1);
    }

    @Test
    void 저장_유효한입력_리뷰저장및반환() {
        // given
        ReviewRequestDTO requestDTO = new ReviewRequestDTO(
                5, "정말 좋았습니다!", LocalDate.now(), testCustomer);

        // when
        Review saved = controller.save(requestDTO);

        // then
        assertNotNull(saved);
        assertEquals(5, saved.getRate());
        assertEquals("정말 좋았습니다!", saved.getContent());
        assertEquals(testCustomer.getId(), saved.getCustomer().getId());
        assertTrue(saved.getId() > 0);
    }

    @Test
    void 저장_잘못된평점_예외발생() {
        // given
        ReviewRequestDTO requestDTO = new ReviewRequestDTO(
                6, "내용", LocalDate.now(), testCustomer);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            controller.save(requestDTO);
        });
    }

    @Test
    void 저장_0점평점_예외발생() {
        // given
        ReviewRequestDTO requestDTO = new ReviewRequestDTO(
                0, "내용", LocalDate.now(), testCustomer);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            controller.save(requestDTO);
        });
    }

    @Test
    void 아이디검색_존재하는아이디_리뷰반환() {
        // given
        ReviewRequestDTO requestDTO = new ReviewRequestDTO(
                4, "좋은 경험이었습니다.", LocalDate.now(), testCustomer);
        Review saved = controller.save(requestDTO);

        // when
        Review found = controller.findById(saved.getId());

        // then
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(4, found.getRate());
    }

    @Test
    void 아이디검색_존재하지않는아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(999);
        });
    }

    @Test
    void 전체조회_여러리뷰존재_모든리뷰반환() {
        // given
        ReviewRequestDTO review1 = new ReviewRequestDTO(
                5, "최고입니다!", LocalDate.now(), testCustomer);
        ReviewRequestDTO review2 = new ReviewRequestDTO(
                4, "좋아요", LocalDate.now(), testCustomer);

        controller.save(review1);
        controller.save(review2);

        // when
        List<Review> all = controller.findAll();

        // then
        assertEquals(2, all.size());
    }

    @Test
    void 고객별조회_특정고객_해당고객리뷰들반환() {
        // given
        Customer anotherCustomer = new Customer("다른유저", "another", "password", "010-9999-9999", "another@example.com", 100000);
        anotherCustomer.setId(2);

        ReviewRequestDTO review1 = new ReviewRequestDTO(
                5, "좋아요", LocalDate.now(), testCustomer);
        ReviewRequestDTO review2 = new ReviewRequestDTO(
                4, "괜찮아요", LocalDate.now(), testCustomer);
        ReviewRequestDTO review3 = new ReviewRequestDTO(
                3, "보통이에요", LocalDate.now(), anotherCustomer);

        controller.save(review1);
        controller.save(review2);
        controller.save(review3);

        // when
        List<Review> customerReviews = controller.findByCustomer(testCustomer);

        // then
        assertEquals(2, customerReviews.size());
        assertTrue(customerReviews.stream()
                .allMatch(r -> r.getCustomer().getId() == testCustomer.getId()));
    }

    @Test
    void 평점별조회_특정평점_해당평점리뷰들반환() {
        // given
        ReviewRequestDTO review1 = new ReviewRequestDTO(
                5, "최고", LocalDate.now(), testCustomer);
        ReviewRequestDTO review2 = new ReviewRequestDTO(
                4, "좋음", LocalDate.now(), testCustomer);
        ReviewRequestDTO review3 = new ReviewRequestDTO(
                5, "훌륭", LocalDate.now(), testCustomer);

        controller.save(review1);
        controller.save(review2);
        controller.save(review3);

        // when
        List<Review> fiveStarReviews = controller.findByRate(5);

        // then
        assertEquals(2, fiveStarReviews.size());
        assertTrue(fiveStarReviews.stream().allMatch(r -> r.getRate() == 5));
    }

    @Test
    void 평점이상조회_특정평점이상_해당리뷰들반환() {
        // given
        ReviewRequestDTO review1 = new ReviewRequestDTO(
                5, "최고", LocalDate.now(), testCustomer);
        ReviewRequestDTO review2 = new ReviewRequestDTO(
                3, "보통", LocalDate.now(), testCustomer);
        ReviewRequestDTO review3 = new ReviewRequestDTO(
                4, "좋음", LocalDate.now(), testCustomer);

        controller.save(review1);
        controller.save(review2);
        controller.save(review3);

        // when
        List<Review> highRatedReviews = controller.findByRateGreaterThanOrEqual(4);

        // then
        assertEquals(2, highRatedReviews.size());
        assertTrue(highRatedReviews.stream().allMatch(r -> r.getRate() >= 4));
    }

    @Test
    void 리뷰수정_유효한입력_리뷰업데이트() {
        // given
        ReviewRequestDTO requestDTO = new ReviewRequestDTO(
                3, "보통이에요", LocalDate.now(), testCustomer);
        Review saved = controller.save(requestDTO);

        // when
        Review updated = controller.updateReview(saved.getId(), 5, "다시 생각해보니 최고였어요!");

        // then
        assertEquals(5, updated.getRate());
        assertEquals("다시 생각해보니 최고였어요!", updated.getContent());
    }

    @Test
    void 리뷰수정_잘못된평점_예외발생() {
        // given
        ReviewRequestDTO requestDTO = new ReviewRequestDTO(
                3, "보통이에요", LocalDate.now(), testCustomer);
        Review saved = controller.save(requestDTO);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            controller.updateReview(saved.getId(), 6, "내용");
        });
    }

    @Test
    void 삭제_존재하는아이디_리뷰삭제() {
        // given
        ReviewRequestDTO requestDTO = new ReviewRequestDTO(
                4, "좋아요", LocalDate.now(), testCustomer);
        Review saved = controller.save(requestDTO);

        // when
        controller.deleteById(saved.getId());

        // then
        List<Review> all = repository.findAll();
        assertTrue(all.isEmpty());
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(saved.getId());
        });
    }

    @Test
    void 삭제_존재하지않는아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.deleteById(999);
        });
    }

    @Test
    void 평균평점조회_리뷰존재_평균값반환() {
        // given
        ReviewRequestDTO review1 = new ReviewRequestDTO(
                5, "최고", LocalDate.now(), testCustomer);
        ReviewRequestDTO review2 = new ReviewRequestDTO(
                3, "보통", LocalDate.now(), testCustomer);
        ReviewRequestDTO review3 = new ReviewRequestDTO(
                4, "좋음", LocalDate.now(), testCustomer);

        controller.save(review1);
        controller.save(review2);
        controller.save(review3);

        // when
        double average = controller.getAverageRate();

        // then
        assertEquals(4.0, average, 0.01);
    }

    @Test
    void 평균평점조회_리뷰없음_0반환() {
        // when
        double average = controller.getAverageRate();

        // then
        assertEquals(0.0, average);
    }

    @Test
    void 날짜검증_리뷰생성시_날짜저장확인() {
        // given
        LocalDate testDate = LocalDate.of(2025, 11, 9);
        ReviewRequestDTO requestDTO = new ReviewRequestDTO(
                5, "테스트", testDate, testCustomer);

        // when
        Review saved = controller.save(requestDTO);

        // then
        assertEquals(testDate, saved.getDate());
    }
}
