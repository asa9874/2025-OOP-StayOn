package org.example.domain.cleaningStaff;

import org.example.Init;
import org.example.domain.cleaningStaff.dto.CleaningStaffRequestDTO;
import org.example.domain.cleaningStaff.strategy.EmptyListStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CleaningStaffTest {
    private CleaningStaffController controller;
    private CleaningStaffRepository repository;

    @BeforeEach
    void setUp() {
        Init.initializeCleaningStaffModule(new EmptyListStrategy());
        repository = CleaningStaffRepository.getInstance();
        controller = CleaningStaffController.getInstance();
        repository.returnToDefaultData();
    }

    @Test
    void 저장_유효한입력_청소스태프저장및반환() {
        // given
        CleaningStaffRequestDTO requestDTO = new CleaningStaffRequestDTO("홍길동", "010-1234-5678");

        // when
        controller.save(requestDTO);

        // then
        List<CleaningStaff> allStaff = repository.findAll();
        assertEquals(1, allStaff.size());

        CleaningStaff staff = allStaff.get(0);
        assertEquals("홍길동", staff.getName());
        assertEquals("010-1234-5678", staff.getPhoneNumber());
        assertTrue(staff.getId() > 0);
    }

    @Test
    void 아이디검색_존재하는아이디_청소스태프반환() {
        // given
        CleaningStaffRequestDTO requestDTO = new CleaningStaffRequestDTO("홍길동", "010-1234-5678");
        CleaningStaff savedStaff = controller.save(requestDTO);

        // when
        CleaningStaff foundStaff = controller.findById(savedStaff.getId());

        // then
        assertNotNull(foundStaff);
        assertEquals("홍길동", foundStaff.getName());
        assertEquals("010-1234-5678", foundStaff.getPhoneNumber());
    }

    @Test
    void 아이디검색_존재하지않는아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(999);
        });
    }

    @Test
    void 아이디삭제_존재하는아이디_청소스태프삭제() {
        // given
        CleaningStaffRequestDTO requestDTO = new CleaningStaffRequestDTO("홍길동", "010-1234-5678");
        CleaningStaff savedStaff = controller.save(requestDTO);

        // when
        controller.deleteById(savedStaff.getId());

        // then
        List<CleaningStaff> allStaff = repository.findAll();
        assertTrue(allStaff.isEmpty());
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(savedStaff.getId());
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
    void 전체조회_여러스태프존재_모든스태프반환() {
        // given
        CleaningStaffRequestDTO staff1 = new CleaningStaffRequestDTO("홍길동", "010-1234-5678");
        CleaningStaffRequestDTO staff2 = new CleaningStaffRequestDTO("김철수", "010-8765-4321");

        controller.save(staff1);
        controller.save(staff2);

        // when
        List<CleaningStaff> allStaff = controller.findAll();

        // then
        assertEquals(2, allStaff.size());
        assertEquals("홍길동", allStaff.get(0).getName());
        assertEquals("김철수", allStaff.get(1).getName());
    }
}