package org.example.domain.pension;

import org.example.Init;
import org.example.domain.pension.dto.PensionRequestDTO;
import org.example.domain.pension.dto.PensionUpdateDTO;
import org.example.domain.pension.strategy.EmptyPensionListStrategy;
import org.example.domain.user.pensionManager.strategy.DefaultPensionManagerDataStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PensionTest {
    private PensionController controller;
    private PensionRepository repository;

    @BeforeEach
    void setUp() {
        // PensionManager 먼저 초기화 (Pension이 PensionManager를 참조함)
        Init.initializePensionManagerModule(new DefaultPensionManagerDataStrategy());
        Init.initializePensionModule(new EmptyPensionListStrategy());
        repository = PensionRepository.getInstance();
        controller = PensionController.getInstance();
        repository.returnToDefaultData();
    }

    @Test
    void 저장_유효한입력_펜션저장및반환() {
        // given
        PensionRequestDTO requestDTO = new PensionRequestDTO(
            "바다뷰 펜션",
            "강원도 강릉시 해안로 123",
            "033-1234-5678",
            "아름다운 바다 전망을 자랑하는 펜션입니다.",
            1,
            "image/pension.png"
        );

        // when
        Pension savedPension = controller.save(requestDTO);

        // then
        List<Pension> allPensions = repository.findAll();
        assertEquals(1, allPensions.size());

        Pension pension = allPensions.get(0);
        assertEquals("바다뷰 펜션", pension.getName());
        assertEquals("강원도 강릉시 해안로 123", pension.getAddress());
        assertEquals("033-1234-5678", pension.getPhoneNumber());
        assertEquals("아름다운 바다 전망을 자랑하는 펜션입니다.", pension.getDescription());
        assertEquals(1, pension.getPensionManagerId());
        assertTrue(pension.getId() > 0);
        assertEquals(savedPension.getId(), pension.getId());
    }

    @Test
    void 아이디검색_존재하는아이디_펜션반환() {
        // given
        PensionRequestDTO requestDTO = new PensionRequestDTO(
            "바다뷰 펜션",
            "강원도 강릉시 해안로 123",
            "033-1234-5678",
            "아름다운 바다 전망을 자랑하는 펜션입니다.",
            1,
            "image/pension.png"
        );
        Pension savedPension = controller.save(requestDTO);

        // when
        Pension foundPension = controller.findById(savedPension.getId());

        // then
        assertNotNull(foundPension);
        assertEquals("바다뷰 펜션", foundPension.getName());
        assertEquals("강원도 강릉시 해안로 123", foundPension.getAddress());
        assertEquals("033-1234-5678", foundPension.getPhoneNumber());
        assertEquals(1, foundPension.getPensionManagerId());
    }

    @Test
    void 아이디검색_존재하지않는아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(999);
        });
    }

    @Test
    void 전체조회_여러펜션존재_모든펜션반환() {
        // given
        PensionRequestDTO pension1 = new PensionRequestDTO(
            "바다뷰 펜션",
            "강원도 강릉시 해안로 123",
            "033-1234-5678",
            "아름다운 바다 전망",
            1,
            "image/pension.png"
        );
        PensionRequestDTO pension2 = new PensionRequestDTO(
            "산속의 휴식",
            "경기도 가평군 산속로 456",
            "031-9876-5432",
            "조용한 산속 힐링",
            1,
            "image/pension.png"
        );
        PensionRequestDTO pension3 = new PensionRequestDTO(
            "제주 힐링 펜션",
            "제주특별자치도 서귀포시 중문로 789",
            "064-7777-8888",
            "제주의 자연 만끽",
            2,
            "image/pension.png"
        );

        controller.save(pension1);
        controller.save(pension2);
        controller.save(pension3);

        // when
        List<Pension> allPensions = controller.findAll();

        // then
        assertEquals(3, allPensions.size());
        assertEquals("바다뷰 펜션", allPensions.get(0).getName());
        assertEquals("산속의 휴식", allPensions.get(1).getName());
        assertEquals("제주 힐링 펜션", allPensions.get(2).getName());
    }

    @Test
    void 운영자별조회_특정운영자의펜션들_해당펜션목록반환() {
        // given
        PensionRequestDTO pension1 = new PensionRequestDTO(
            "바다뷰 펜션",
            "강원도 강릉시 해안로 123",
            "033-1234-5678",
            "아름다운 바다 전망",
            1,
            "image/pension.png"
        );
        PensionRequestDTO pension2 = new PensionRequestDTO(
            "산속의 휴식",
            "경기도 가평군 산속로 456",
            "031-9876-5432",
            "조용한 산속 힐링",
            1,
            "image/pension.png"
        );
        PensionRequestDTO pension3 = new PensionRequestDTO(
            "제주 힐링 펜션",
            "제주특별자치도 서귀포시 중문로 789",
            "064-7777-8888",
            "제주의 자연 만끽",
            2,
            "image/pension.png"
        );

        controller.save(pension1);
        controller.save(pension2);
        controller.save(pension3);

        // when
        List<Pension> manager1Pensions = controller.findByPensionManagerId(1);
        List<Pension> manager2Pensions = controller.findByPensionManagerId(2);

        // then
        assertEquals(2, manager1Pensions.size());
        assertEquals(1, manager2Pensions.size());
        assertEquals("바다뷰 펜션", manager1Pensions.get(0).getName());
        assertEquals("산속의 휴식", manager1Pensions.get(1).getName());
        assertEquals("제주 힐링 펜션", manager2Pensions.get(0).getName());
    }

    @Test
    void 수정_존재하는펜션_펜션정보수정됨() {
        // given
        PensionRequestDTO requestDTO = new PensionRequestDTO(
            "바다뷰 펜션",
            "강원도 강릉시 해안로 123",
            "033-1234-5678",
            "아름다운 바다 전망",
            1,
            "image/pension.png"
        );
        Pension savedPension = controller.save(requestDTO);

        PensionUpdateDTO updateDTO = new PensionUpdateDTO(
            savedPension.getId(),
            "오션뷰 리조트",
            "강원도 강릉시 해안로 123-1",
            "033-1234-9999",
            "업그레이드된 바다 전망 펜션"
        );

        // when
        Pension updatedPension = controller.update(updateDTO);

        // then
        assertNotNull(updatedPension);
        assertEquals(savedPension.getId(), updatedPension.getId());
        assertEquals("오션뷰 리조트", updatedPension.getName());
        assertEquals("강원도 강릉시 해안로 123-1", updatedPension.getAddress());
        assertEquals("033-1234-9999", updatedPension.getPhoneNumber());
        assertEquals("업그레이드된 바다 전망 펜션", updatedPension.getDescription());
        assertEquals(1, updatedPension.getPensionManagerId()); // 운영자는 변경되지 않음
    }

    @Test
    void 수정_존재하지않는펜션_예외발생() {
        // given
        PensionUpdateDTO updateDTO = new PensionUpdateDTO(
            999,
            "존재하지 않는 펜션",
            "주소",
            "전화번호",
            "설명"
        );

        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.update(updateDTO);
        });
    }

    @Test
    void 아이디삭제_존재하는아이디_펜션삭제() {
        // given
        PensionRequestDTO requestDTO = new PensionRequestDTO(
            "바다뷰 펜션",
            "강원도 강릉시 해안로 123",
            "033-1234-5678",
            "아름다운 바다 전망",
            1,
            "image/pension.png"
        );
        Pension savedPension = controller.save(requestDTO);

        // when
        controller.deleteById(savedPension.getId());

        // then
        List<Pension> allPensions = repository.findAll();
        assertTrue(allPensions.isEmpty());
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(savedPension.getId());
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
    void 운영자별조회_펜션이없는운영자_빈목록반환() {
        // given
        PensionRequestDTO requestDTO = new PensionRequestDTO(
            "바다뷰 펜션",
            "강원도 강릉시 해안로 123",
            "033-1234-5678",
            "아름다운 바다 전망",
            1,
            "image/pension.png"
        );
        controller.save(requestDTO);

        // when
        List<Pension> manager3Pensions = controller.findByPensionManagerId(3);

        // then
        assertTrue(manager3Pensions.isEmpty());
    }

    @Test
    void 저장_여러펜션저장_아이디자동증가확인() {
        // given
        PensionRequestDTO pension1 = new PensionRequestDTO(
            "펜션1", "주소1", "010-1111-1111", "설명1", 1, "image/pension.png"
        );
        PensionRequestDTO pension2 = new PensionRequestDTO(
            "펜션2", "주소2", "010-2222-2222", "설명2", 1, "image/pension.png"
        );
        PensionRequestDTO pension3 = new PensionRequestDTO(
            "펜션3", "주소3", "010-3333-3333", "설명3", 1, "image/pension.png"
        );

        // when
        Pension saved1 = controller.save(pension1);
        Pension saved2 = controller.save(pension2);
        Pension saved3 = controller.save(pension3);

        // then
        assertTrue(saved1.getId() > 0);
        assertEquals(saved1.getId() + 1, saved2.getId());
        assertEquals(saved2.getId() + 1, saved3.getId());
    }
}
