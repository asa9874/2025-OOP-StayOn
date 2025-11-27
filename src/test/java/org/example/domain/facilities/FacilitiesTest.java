package org.example.domain.facilities;

import org.example.Init;
import org.example.domain.facilities.dto.FacilitiesRequestDTO;
import org.example.domain.facilities.strategy.EmptyFacilitiesListStrategy;
import org.example.domain.pension.strategy.DefaultPensionDataStrategy;
import org.example.domain.user.pensionManager.strategy.DefaultPensionManagerDataStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class FacilitiesTest {
    private FacilitiesController controller;
    private FacilitiesRepository repository;

    @BeforeEach
    void setUp() {
        // PensionManager와 Pension 먼저 초기화 (Facilities가 Pension을 참조함)
        Init.initializePensionManagerModule(new DefaultPensionManagerDataStrategy());
        Init.initializePensionModule(new DefaultPensionDataStrategy());
        Init.initializeFacilitiesModule(new EmptyFacilitiesListStrategy());
        repository = FacilitiesRepository.getInstance();
        controller = FacilitiesController.getInstance();
        repository.returnToDefaultData();
    }    
    @Test
    void 저장_유효한입력_시설저장및반환() {
        // given
        LocalDateTime openingTime = LocalDateTime.of(2023, 1, 1, 9, 0);
        LocalDateTime closingTime = LocalDateTime.of(2023, 1, 1, 18, 0);
        FacilitiesRequestDTO requestDTO = new FacilitiesRequestDTO(
            "수영장",
            openingTime,
            closingTime,
            true,
            1, // pensionId
            "src/main/java/org/example/image/facilities/pool.jpeg"
        );

        // when
        Facilities savedFacility = controller.save(requestDTO);

        // then
        List<Facilities> allFacilities = repository.findAll();
        assertEquals(1, allFacilities.size());

        Facilities facility = allFacilities.get(0);
        assertEquals("수영장", facility.getName());
        assertEquals(openingTime, facility.getOpeningTime());
        assertEquals(closingTime, facility.getClosingTime());
        assertTrue(facility.isRequireReservation());
        assertEquals(1, facility.getPension().getId());
        assertTrue(facility.getId() > 0);
    }    
    @Test
    void 아이디검색_존재하는아이디_시설반환() {
        // given
        LocalDateTime openingTime = LocalDateTime.of(2023, 1, 1, 9, 0);
        LocalDateTime closingTime = LocalDateTime.of(2023, 1, 1, 18, 0);
        FacilitiesRequestDTO requestDTO = new FacilitiesRequestDTO(
            "수영장",
            openingTime,
            closingTime,
            true,
            1,
            "src/main/java/org/example/image/facilities/pool.jpeg"
        );
        Facilities savedFacility = controller.save(requestDTO);

        // when
        Facilities foundFacility = controller.findById(savedFacility.getId());

        // then
        assertNotNull(foundFacility);
        assertEquals("수영장", foundFacility.getName());
        assertEquals(openingTime, foundFacility.getOpeningTime());
        assertEquals(closingTime, foundFacility.getClosingTime());
        assertTrue(foundFacility.isRequireReservation());
        assertEquals(1, foundFacility.getPension().getId());
    }

    @Test
    void 아이디검색_존재하지않는아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(999);
        });
    }    
    @Test
    void 아이디삭제_존재하는아이디_시설삭제() {
        // given
        LocalDateTime openingTime = LocalDateTime.of(2023, 1, 1, 9, 0);
        LocalDateTime closingTime = LocalDateTime.of(2023, 1, 1, 18, 0);
        FacilitiesRequestDTO requestDTO = new FacilitiesRequestDTO(
            "수영장",
            openingTime,
            closingTime,
            true,
            1,
            "src/main/java/org/example/image/facilities/pool.jpeg"
        );
        Facilities savedFacility = controller.save(requestDTO);

        // when
        controller.deleteById(savedFacility.getId());

        // then
        List<Facilities> allFacilities = repository.findAll();
        assertTrue(allFacilities.isEmpty());
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(savedFacility.getId());
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
    void 전체조회_여러시설존재_모든시설반환() {
        // given
        LocalDateTime openingTime = LocalDateTime.of(2023, 1, 1, 9, 0);
        LocalDateTime closingTime = LocalDateTime.of(2023, 1, 1, 18, 0);
        FacilitiesRequestDTO facility1 = new FacilitiesRequestDTO(
            "수영장",
            openingTime,
            closingTime,
            true,
            1,
            "src/main/java/org/example/image/facilities/pool.jpeg"
        );
        FacilitiesRequestDTO facility2 = new FacilitiesRequestDTO(
            "사우나",
            openingTime,
            closingTime,
            false,
            1,
            "src/main/java/org/example/image/facilities/sauna.jpeg"
        );

        controller.save(facility1);
        controller.save(facility2);

        // when
        List<Facilities> allFacilities = controller.findAll();

        // then
        assertEquals(2, allFacilities.size());
    }    
    @Test
    void 펜션아이디로조회_존재하는펜션아이디_해당시설반환() {
        // given
        LocalDateTime openingTime = LocalDateTime.of(2023, 1, 1, 9, 0);
        LocalDateTime closingTime = LocalDateTime.of(2023, 1, 1, 18, 0);
        FacilitiesRequestDTO requestDTO = new FacilitiesRequestDTO(
            "수영장",
            openingTime,
            closingTime,
            true,
            1,
            "src/main/java/org/example/image/facilities/pool.jpeg"
        );
        controller.save(requestDTO);

        // when
        List<Facilities> facilities = controller.findByPensionId(1);

        // then
        assertEquals(1, facilities.size());
        assertEquals("수영장", facilities.get(0).getName());
    }

    @Test
    void 펜션아이디로조회_존재하지않는펜션아이디_빈리스트반환() {
        // when
        List<Facilities> facilities = controller.findByPensionId(999);

        // then
        assertTrue(facilities.isEmpty());
    }
}
