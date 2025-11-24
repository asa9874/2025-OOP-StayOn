package org.example;

import org.example.domain.cleaningStaff.CleaningStaffController;
import org.example.domain.cleaningStaff.CleaningStaffRepository;
import org.example.domain.cleaningStaff.CleaningStaffService;
import org.example.domain.cleaningStaff.strategy.CleaningStaffInitStrategy;
import org.example.domain.cleaningStaff.strategy.DefaultDataStrategy;
import org.example.domain.facilities.strategy.DefaultFacilitiesDataStrategy;
import org.example.domain.pension.PensionController;
import org.example.domain.pension.PensionRepository;
import org.example.domain.pension.PensionService;
import org.example.domain.pension.strategy.DefaultPensionDataStrategy;
import org.example.domain.pension.strategy.PensionInitStrategy;
import org.example.domain.user.customer.CustomerController;
import org.example.domain.user.customer.CustomerRepository;
import org.example.domain.user.customer.CustomerService;
import org.example.domain.user.customer.strategy.CustomerInitStrategy;
import org.example.domain.user.customer.strategy.DefaultCustomerDataStrategy;
import org.example.domain.user.pensionManager.PensionManagerController;
import org.example.domain.user.pensionManager.PensionManagerRepository;
import org.example.domain.user.pensionManager.PensionManagerService;
import org.example.domain.user.pensionManager.strategy.PensionManagerInitStrategy;
import org.example.domain.user.pensionManager.strategy.DefaultPensionManagerDataStrategy;
import org.example.domain.room.RoomController;
import org.example.domain.room.RoomRepository;
import org.example.domain.room.RoomService;
import org.example.domain.room.strategy.RoomInitStrategy;
import org.example.domain.room.strategy.DefaultRoomDataStrategy;
import org.example.domain.reservation.ReservationController;
import org.example.domain.reservation.ReservationRepository;
import org.example.domain.reservation.ReservationService;
import org.example.domain.reservation.strategy.ReservationInitStrategy;
import org.example.domain.reservation.strategy.DefaultReservationDataStrategy;
import org.example.domain.review.ReviewController;
import org.example.domain.review.ReviewRepository;
import org.example.domain.review.ReviewService;
import org.example.domain.review.strategy.ReviewInitStrategy;
import org.example.domain.review.strategy.DefaultReviewDataStrategy;
import org.example.domain.facilities.FacilitiesController;
import org.example.domain.facilities.FacilitiesRepository;
import org.example.domain.facilities.FacilitiesService;
import org.example.domain.facilities.strategy.FacilitiesInitStrategy;

public class Init {
    public static void initializeDependencies() { // DI 하는 메서드
        // 의존성 순서: PensionManager → Pension → Room, Customer → Reservation, Review
        initializeCleaningStaffModule(new DefaultDataStrategy());
        initializePensionManagerModule(new DefaultPensionManagerDataStrategy());
        initializePensionModule(new DefaultPensionDataStrategy());
        initializeCustomerModule(new DefaultCustomerDataStrategy());
        initializeRoomModule(new DefaultRoomDataStrategy());
        initializeReservationModule(new DefaultReservationDataStrategy());
        initializeReviewModule(new DefaultReviewDataStrategy());
        initializeFacilitiesModule(new DefaultFacilitiesDataStrategy());
    }

    public static void initializeCleaningStaffModule(CleaningStaffInitStrategy strategy) {
        CleaningStaffInitStrategy cleaningStaffInitStrategy;
        if (strategy == null) {
            cleaningStaffInitStrategy = new DefaultDataStrategy();
        }
        cleaningStaffInitStrategy = strategy;
        CleaningStaffRepository.initialize(cleaningStaffInitStrategy);
        CleaningStaffService.initialize(CleaningStaffRepository.getInstance());
        CleaningStaffController.initialize(CleaningStaffService.getInstance());
    }

    public static void initializeCustomerModule(CustomerInitStrategy strategy) {
        CustomerInitStrategy customerInitStrategy;
        if (strategy == null) {
            customerInitStrategy = new DefaultCustomerDataStrategy();
        }
        customerInitStrategy = strategy;
        CustomerRepository.initialize(customerInitStrategy);
        CustomerService.initialize(CustomerRepository.getInstance());
        CustomerController.initialize(CustomerService.getInstance());
    }

    public static void initializePensionManagerModule(PensionManagerInitStrategy strategy) {
        PensionManagerInitStrategy pensionManagerInitStrategy;
        if (strategy == null) {
            pensionManagerInitStrategy = new DefaultPensionManagerDataStrategy();
        }
        pensionManagerInitStrategy = strategy;
        PensionManagerRepository.initialize(pensionManagerInitStrategy);
        PensionManagerService.initialize(PensionManagerRepository.getInstance());
        PensionManagerController.initialize(PensionManagerService.getInstance());
    }

    public static void initializeRoomModule(RoomInitStrategy strategy) {
        RoomInitStrategy roomInitStrategy;
        if (strategy == null) {
            roomInitStrategy = new DefaultRoomDataStrategy();
        }
        roomInitStrategy = strategy;
        RoomRepository.initialize(roomInitStrategy);
        RoomService.initialize(RoomRepository.getInstance());
        RoomController.initialize(RoomService.getInstance());
    }

    public static void initializeReservationModule(ReservationInitStrategy strategy) {
        ReservationInitStrategy reservationInitStrategy;
        if (strategy == null) {
            reservationInitStrategy = new DefaultReservationDataStrategy();
        }
        reservationInitStrategy = strategy;
        ReservationRepository reservationRepository = ReservationRepository.getInstance();
        reservationRepository.setInitStrategy(reservationInitStrategy);

        ReservationService reservationService = ReservationService.getInstance();
        reservationService.setReservationRepository(reservationRepository);

        ReservationController reservationController = ReservationController.getInstance();
        reservationController.setReservationService(reservationService);
    }

    public static void initializeReviewModule(ReviewInitStrategy strategy) {
        ReviewInitStrategy reviewInitStrategy;
        if (strategy == null) {
            reviewInitStrategy = new DefaultReviewDataStrategy();
        }
        reviewInitStrategy = strategy;
        ReviewRepository reviewRepository = ReviewRepository.getInstance();
        reviewRepository.setInitStrategy(reviewInitStrategy);

        ReviewService reviewService = ReviewService.getInstance();
        reviewService.setReviewRepository(reviewRepository);

        ReviewController reviewController = ReviewController.getInstance();
        reviewController.setReviewService(reviewService);
    }

    public static void initializePensionModule(PensionInitStrategy strategy) {
        PensionRepository.initialize(strategy);
        PensionService.initialize(PensionRepository.getInstance());
        PensionController.initialize(PensionService.getInstance());
    }

    public static void initializeFacilitiesModule(FacilitiesInitStrategy strategy) {
        FacilitiesInitStrategy facilitiesInitStrategy;
        if (strategy == null) {
            facilitiesInitStrategy = new DefaultFacilitiesDataStrategy();
        }
        facilitiesInitStrategy = strategy;
        FacilitiesRepository.initialize(facilitiesInitStrategy);
        FacilitiesService.initialize(FacilitiesRepository.getInstance());
        FacilitiesController.initialize(FacilitiesService.getInstance());
    }
}
