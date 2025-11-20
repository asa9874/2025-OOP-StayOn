package org.example.domain.reservation;

import org.example.Init;
import org.example.domain.pension.strategy.DefaultPensionDataStrategy;
import org.example.domain.reservation.dto.ReservationRequestDTO;
import org.example.domain.reservation.strategy.EmptyReservationListStrategy;
import org.example.domain.room.Room;
import org.example.domain.room.RoomStatus;
import org.example.domain.room.RoomType;
import org.example.domain.user.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {
    private ReservationController controller;
    private ReservationRepository repository;
    private Customer testCustomer;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        // 의존성 순서: PensionManager → Pension → Room, Customer → Reservation
        Init.initializePensionManagerModule(new org.example.domain.user.pensionManager.strategy.DefaultPensionManagerDataStrategy());
        Init.initializePensionModule(new DefaultPensionDataStrategy());
        Init.initializeRoomModule(new org.example.domain.room.strategy.DefaultRoomDataStrategy());
        Init.initializeCustomerModule(new org.example.domain.user.customer.strategy.DefaultCustomerDataStrategy());
        Init.initializeReservationModule(new EmptyReservationListStrategy());
        repository = ReservationRepository.getInstance();
        controller = ReservationController.getInstance();
        repository.returnToDefaultData();

        // 테스트용 Customer 생성
        testCustomer = new Customer("테스트유저", "testuser", "password", "010-1234-5678", "test@example.com", 100000);
        testCustomer.setId(1);

        // 테스트용 Room 생성
        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setRoomName("101호");
        testRoom.setFloor(1);
        testRoom.setBuilding("A동");
        testRoom.setMaxPeople(2);
        testRoom.setDescription("테스트룸");
        testRoom.setRoomStatus(RoomStatus.RESERVATION);
        testRoom.setRoomType(RoomType.SINGLE);
        testRoom.setPrice(50000);
    }

    @Test
    void 저장_유효한입력_예약저장및반환() {
        // given
        ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);

        // when
        Reservation saved = controller.save(requestDTO);

        // then
        assertNotNull(saved);
        assertEquals(testRoom.getId(), saved.getRoom().getId());
        assertEquals(testCustomer.getId(), saved.getCustomer().getId());
        assertEquals(ReservationStatus.PENDING, saved.getReservationStatus());
        assertTrue(saved.getId() > 0);
    }

    @Test
    void 아이디검색_존재하는아이디_예약반환() {
        // given
        ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);
        Reservation saved = controller.save(requestDTO);

        // when
        Reservation found = controller.findById(saved.getId());

        // then
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(testRoom.getId(), found.getRoom().getId());
    }

    @Test
    void 아이디검색_존재하지않는아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(999);
        });
    }

    @Test
    void 전체조회_여러예약존재_모든예약반환() {
        // given
        ReservationRequestDTO request1 = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);
        ReservationRequestDTO request2 = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.CONFIRMED);

        controller.save(request1);
        controller.save(request2);

        // when
        List<Reservation> all = controller.findAll();

        // then
        assertEquals(2, all.size());
    }

    @Test
    void 고객별조회_특정고객_해당고객예약들반환() {
        // given
        Customer anotherCustomer = new Customer("다른유저", "another", "password", "010-9999-9999", "another@example.com", 100000);
        anotherCustomer.setId(2);

        ReservationRequestDTO request1 = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);
        ReservationRequestDTO request2 = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.CONFIRMED);
        ReservationRequestDTO request3 = new ReservationRequestDTO(
                testRoom, anotherCustomer, ReservationStatus.PENDING);

        controller.save(request1);
        controller.save(request2);
        controller.save(request3);

        // when
        List<Reservation> customerReservations = controller.findByCustomer(testCustomer);

        // then
        assertEquals(2, customerReservations.size());
        assertTrue(customerReservations.stream()
                .allMatch(r -> r.getCustomer().getId() == testCustomer.getId()));
    }

    @Test
    void 객실별조회_특정객실_해당객실예약들반환() {
        // given
        Room anotherRoom = new Room();
        anotherRoom.setId(2);
        anotherRoom.setPrice(80000);

        ReservationRequestDTO request1 = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);
        ReservationRequestDTO request2 = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.CONFIRMED);
        ReservationRequestDTO request3 = new ReservationRequestDTO(
                anotherRoom, testCustomer, ReservationStatus.PENDING);

        controller.save(request1);
        controller.save(request2);
        controller.save(request3);

        // when
        List<Reservation> roomReservations = controller.findByRoom(testRoom);

        // then
        assertEquals(2, roomReservations.size());
        assertTrue(roomReservations.stream()
                .allMatch(r -> r.getRoom().getId() == testRoom.getId()));
    }

    @Test
    void 상태별조회_특정상태_해당상태예약들반환() {
        // given
        ReservationRequestDTO request1 = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);
        ReservationRequestDTO request2 = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.CONFIRMED);
        ReservationRequestDTO request3 = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);

        controller.save(request1);
        controller.save(request2);
        controller.save(request3);

        // when
        List<Reservation> pendingReservations = controller.findByStatus(ReservationStatus.PENDING);

        // then
        assertEquals(2, pendingReservations.size());
        assertTrue(pendingReservations.stream()
                .allMatch(r -> r.getReservationStatus() == ReservationStatus.PENDING));
    }

    @Test
    void 결제_대기상태_결제완료및잔액차감() {
        // given
        ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);
        Reservation saved = controller.save(requestDTO);
        int initialMoney = testCustomer.getMoney();

        // when
        Reservation paid = controller.pay(saved.getId());

        // then
        assertEquals(ReservationStatus.CONFIRMED, paid.getReservationStatus());
        assertEquals(initialMoney - testRoom.getPrice(), testCustomer.getMoney());
    }

    @Test
    void 결제_대기상태아님_예외발생() {
        // given
        ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.CONFIRMED);
        Reservation saved = controller.save(requestDTO);

        // when & then
        assertThrows(IllegalStateException.class, () -> {
            controller.pay(saved.getId());
        });
    }

    @Test
    void 결제_잔액부족_예외발생() {
        // given
        testCustomer.setMoney(10000); // 룸 가격(50000)보다 적게 설정
        ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);
        Reservation saved = controller.save(requestDTO);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            controller.pay(saved.getId());
        });
    }

    @Test
    void 취소_대기상태_취소완료() {
        // given
        ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);
        Reservation saved = controller.save(requestDTO);

        // when
        Reservation cancelled = controller.cancel(saved.getId());

        // then
        assertEquals(ReservationStatus.CANCELLED, cancelled.getReservationStatus());
    }

    @Test
    void 취소_확정상태_취소완료() {
        // given
        ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.CONFIRMED);
        Reservation saved = controller.save(requestDTO);

        // when
        Reservation cancelled = controller.cancel(saved.getId());

        // then
        assertEquals(ReservationStatus.CANCELLED, cancelled.getReservationStatus());
    }

    @Test
    void 취소_이미취소된예약_예외발생() {
        // given
        ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.CANCELLED);
        Reservation saved = controller.save(requestDTO);

        // when & then
        assertThrows(IllegalStateException.class, () -> {
            controller.cancel(saved.getId());
        });
    }

    @Test
    void 환불_확정상태_환불완료및잔액증가() {
        // given
        ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.CONFIRMED);
        Reservation saved = controller.save(requestDTO);
        int initialMoney = testCustomer.getMoney();

        // when
        Reservation refunded = controller.refund(saved.getId());

        // then
        assertEquals(ReservationStatus.REFUNDED, refunded.getReservationStatus());
        assertEquals(initialMoney + testRoom.getPrice(), testCustomer.getMoney());
    }

    @Test
    void 환불_확정상태아님_예외발생() {
        // given
        ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);
        Reservation saved = controller.save(requestDTO);

        // when & then
        assertThrows(IllegalStateException.class, () -> {
            controller.refund(saved.getId());
        });
    }

    @Test
    void 삭제_존재하는아이디_예약삭제() {
        // given
        ReservationRequestDTO requestDTO = new ReservationRequestDTO(
                testRoom, testCustomer, ReservationStatus.PENDING);
        Reservation saved = controller.save(requestDTO);

        // when
        controller.deleteById(saved.getId());

        // then
        List<Reservation> all = repository.findAll();
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
}
