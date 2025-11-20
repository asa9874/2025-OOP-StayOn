package org.example.domain.room;

import org.example.Init;
import org.example.domain.room.dto.RoomRequestDTO;
import org.example.domain.room.strategy.EmptyRoomListStrategy;
import org.example.domain.user.pensionManager.strategy.DefaultPensionManagerDataStrategy;
import org.example.domain.pension.strategy.DefaultPensionDataStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    private RoomController controller;
    private RoomRepository repository;

    @BeforeEach
    void setUp() {
        // 의존성 순서: PensionManager → Pension → Room
        Init.initializePensionManagerModule(new DefaultPensionManagerDataStrategy());
        Init.initializePensionModule(new DefaultPensionDataStrategy());
        Init.initializeRoomModule(new EmptyRoomListStrategy());
        repository = RoomRepository.getInstance();
        controller = RoomController.getInstance();
        repository.returnToDefaultData();
    }

    @Test
    void 저장_유효한입력_객실저장및반환() {
        // given
        RoomRequestDTO requestDTO = new RoomRequestDTO(
                "101호", 1, "A동", 2, "아늑한 싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");

        // when
        controller.save(requestDTO);

        // then
        List<Room> allRooms = repository.findAll();
        assertEquals(1, allRooms.size());

        Room room = allRooms.get(0);
        assertEquals("101호", room.getRoomName());
        assertEquals(1, room.getFloor());
        assertEquals("A동", room.getBuilding());
        assertEquals(2, room.getMaxPeople());
        assertEquals("아늑한 싱글룸", room.getDescription());
        assertEquals(RoomStatus.RESERVATION, room.getRoomStatus());
        assertEquals(RoomType.SINGLE, room.getRoomType());
        assertEquals(50000, room.getPrice());
        assertEquals(1, room.getPensionId());
        assertEquals("image/room.png", room.getImage());
        assertTrue(room.getId() > 0);
    }

    @Test
    void 아이디검색_존재하는아이디_객실반환() {
        // given
        RoomRequestDTO requestDTO = new RoomRequestDTO(
                "101호", 1, "A동", 2, "아늑한 싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        Room savedRoom = controller.save(requestDTO);

        // when
        Room foundRoom = controller.findById(savedRoom.getId());

        // then
        assertNotNull(foundRoom);
        assertEquals("101호", foundRoom.getRoomName());
        assertEquals(RoomType.SINGLE, foundRoom.getRoomType());
        assertEquals(50000, foundRoom.getPrice());
    }

    @Test
    void 아이디검색_존재하지않는아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(999);
        });
    }

    @Test
    void 객실명검색_존재하는객실명_객실반환() {
        // given
        RoomRequestDTO requestDTO = new RoomRequestDTO(
                "101호", 1, "A동", 2, "아늑한 싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        controller.save(requestDTO);

        // when
        Room foundRoom = controller.findByRoomName("101호");

        // then
        assertNotNull(foundRoom);
        assertEquals("101호", foundRoom.getRoomName());
    }

    @Test
    void 객실명검색_존재하지않는객실명_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.findByRoomName("999호");
        });
    }

    @Test
    void 삭제_존재하는객실_객실삭제() {
        // given
        RoomRequestDTO requestDTO = new RoomRequestDTO(
                "101호", 1, "A동", 2, "싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        Room savedRoom = controller.save(requestDTO);

        // when
        controller.deleteById(savedRoom.getId());

        // then
        List<Room> allRooms = repository.findAll();
        assertTrue(allRooms.isEmpty());
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(savedRoom.getId());
        });
    }

    @Test
    void 아이디삭제_존재하지않는아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.deleteById(999);
        });
    }

    @Test
    void 전체조회_저장된객실목록_모든객실반환() {
        // given
        RoomRequestDTO room1 = new RoomRequestDTO(
                "101호", 1, "A동", 2, "싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        RoomRequestDTO room2 = new RoomRequestDTO(
                "201호", 2, "A동", 4, "복층객실", RoomStatus.RESERVATION, RoomType.DUPLEX, 100000, 1, "image/room.png");

        controller.save(room1);
        controller.save(room2);

        // when
        List<Room> allRooms = controller.findAll();

        // then
        assertEquals(2, allRooms.size());
        assertEquals("101호", allRooms.get(0).getRoomName());
        assertEquals("201호", allRooms.get(1).getRoomName());
    }

    @Test
    void 객실타입별조회_특정타입_해당타입객실들반환() {
        // given
        RoomRequestDTO room1 = new RoomRequestDTO(
                "101호", 1, "A동", 2, "싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        RoomRequestDTO room2 = new RoomRequestDTO(
                "201호", 2, "A동", 4, "복층객실", RoomStatus.RESERVATION, RoomType.DUPLEX, 100000, 1, "image/room.png");
        RoomRequestDTO room3 = new RoomRequestDTO(
                "102호", 1, "A동", 2, "싱글룸2", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");

        controller.save(room1);
        controller.save(room2);
        controller.save(room3);

        // when
        List<Room> singleRooms = controller.findByRoomType(RoomType.SINGLE);

        // then
        assertEquals(2, singleRooms.size());
        assertTrue(singleRooms.stream().allMatch(room -> room.getRoomType() == RoomType.SINGLE));
    }

    @Test
    void 객실상태별조회_특정상태_해당상태객실들반환() {
        // given
        RoomRequestDTO room1 = new RoomRequestDTO(
                "101호", 1, "A동", 2, "싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        RoomRequestDTO room2 = new RoomRequestDTO(
                "201호", 2, "A동", 4, "복층객실", RoomStatus.USING, RoomType.DUPLEX, 100000, 1, "image/room.png");
        RoomRequestDTO room3 = new RoomRequestDTO(
                "102호", 1, "A동", 2, "싱글룸2", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");

        controller.save(room1);
        controller.save(room2);
        controller.save(room3);

        // when
        List<Room> reservationRooms = controller.findByRoomStatus(RoomStatus.RESERVATION);

        // then
        assertEquals(2, reservationRooms.size());
        assertTrue(reservationRooms.stream().allMatch(room -> room.getRoomStatus() == RoomStatus.RESERVATION));
    }

    @Test
    void 객실상태변경_유효한상태_상태업데이트() {
        // given
        RoomRequestDTO requestDTO = new RoomRequestDTO(
                "101호", 1, "A동", 2, "싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        Room savedRoom = controller.save(requestDTO);

        // when
        Room updatedRoom = controller.updateRoomStatus(savedRoom.getId(), RoomStatus.USING);

        // then
        assertEquals(RoomStatus.USING, updatedRoom.getRoomStatus());
        assertEquals(RoomStatus.USING, controller.findById(savedRoom.getId()).getRoomStatus());
    }

    @Test
    void 객실상태변경_존재하지않는객실_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.updateRoomStatus(999, RoomStatus.USING);
        });
    }

    @Test
    void 가격조회_유효한객실_가격반환() {
        // given
        RoomRequestDTO requestDTO = new RoomRequestDTO(
                "101호", 1, "A동", 2, "싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        Room savedRoom = controller.save(requestDTO);

        // when
        int price = savedRoom.getPrice();

        // then
        assertEquals(50000, price);
    }

    @Test
    void 가격변경_유효한금액_가격업데이트() {
        // given
        RoomRequestDTO requestDTO = new RoomRequestDTO(
                "101호", 1, "A동", 2, "싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        Room savedRoom = controller.save(requestDTO);

        // when
        savedRoom.setPrice(60000);

        // then
        assertEquals(60000, savedRoom.getPrice());
        assertEquals(60000, controller.findById(savedRoom.getId()).getPrice());
    }

    @Test
    void 펜션별조회_다양한펜션의객실_해당펜션객실목록반환() {
        // given
        RoomRequestDTO room1 = new RoomRequestDTO(
                "101호", 1, "A동", 2, "싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        RoomRequestDTO room2 = new RoomRequestDTO(
                "201호", 2, "A동", 4, "복층객실", RoomStatus.RESERVATION, RoomType.DUPLEX, 100000, 1, "image/room.png");
        RoomRequestDTO room3 = new RoomRequestDTO(
                "301호", 3, "B동", 2, "싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 55000, 2, "image/room.png");

        controller.save(room1);
        controller.save(room2);
        controller.save(room3);

        // when
        List<Room> pension1Rooms = controller.findByPensionId(1);

        // then
        assertEquals(2, pension1Rooms.size());
        assertTrue(pension1Rooms.stream().allMatch(room -> room.getPensionId() == 1));
        assertEquals("101호", pension1Rooms.get(0).getRoomName());
        assertEquals("201호", pension1Rooms.get(1).getRoomName());
    }

    @Test
    void 펜션별조회_존재하지않는펜션_빈리스트반환() {
        // given
        RoomRequestDTO room1 = new RoomRequestDTO(
                "101호", 1, "A동", 2, "싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        controller.save(room1);

        // when
        List<Room> nonExistentPensionRooms = controller.findByPensionId(999);

        // then
        assertTrue(nonExistentPensionRooms.isEmpty());
    }

    @Test
    void 펜션별조회_여러펜션_각펜션별로다른객실반환() {
        // given
        RoomRequestDTO room1 = new RoomRequestDTO(
                "101호", 1, "A동", 2, "싱글룸", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");
        RoomRequestDTO room2 = new RoomRequestDTO(
                "201호", 2, "A동", 4, "복층객실", RoomStatus.RESERVATION, RoomType.DUPLEX, 100000, 2, "image/room.png");
        RoomRequestDTO room3 = new RoomRequestDTO(
                "102호", 1, "A동", 2, "싱글룸2", RoomStatus.RESERVATION, RoomType.SINGLE, 50000, 1, "image/room.png");

        controller.save(room1);
        controller.save(room2);
        controller.save(room3);

        // when
        List<Room> pension1Rooms = controller.findByPensionId(1);
        List<Room> pension2Rooms = controller.findByPensionId(2);

        // then
        assertEquals(2, pension1Rooms.size());
        assertEquals(1, pension2Rooms.size());
        assertTrue(pension1Rooms.stream().allMatch(room -> room.getPensionId() == 1));
        assertTrue(pension2Rooms.stream().allMatch(room -> room.getPensionId() == 2));
    }
}
