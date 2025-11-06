package org.example.domain.user.pensionManager;

import org.example.Init;
import org.example.domain.user.pensionManager.dto.PensionManagerRequestDTO;
import org.example.domain.user.pensionManager.strategy.EmptyPensionManagerListStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PensionManagerTest {
    private PensionManagerController controller;
    private PensionManagerRepository repository;

    @BeforeEach
    void setUp() {
        Init.initializePensionManagerModule(new EmptyPensionManagerListStrategy());
        repository = PensionManagerRepository.getInstance();
        controller = PensionManagerController.getInstance();
        repository.returnToDefaultData();
    }

    @Test
    void 저장_유효한입력_펜션관리자저장및반환() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager@pension.com");

        // when
        controller.save(requestDTO);

        // then
        List<PensionManager> allManagers = repository.findAll();
        assertEquals(1, allManagers.size());

        PensionManager manager = allManagers.get(0);
        assertEquals("이사장", manager.getName());
        assertEquals("manager1", manager.getAccountId());
        assertEquals("010-5555-1111", manager.getPhone());
        assertEquals("manager@pension.com", manager.getEmail());
        assertTrue(manager.getId() > 0);
    }

    @Test
    void 아이디검색_존재하는아이디_펜션관리자반환() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager@pension.com");
        PensionManager savedManager = controller.save(requestDTO);

        // when
        PensionManager foundManager = controller.findById(savedManager.getId());

        // then
        assertNotNull(foundManager);
        assertEquals("이사장", foundManager.getName());
        assertEquals("manager1", foundManager.getAccountId());
        assertEquals("010-5555-1111", foundManager.getPhone());
        assertEquals("manager@pension.com", foundManager.getEmail());
    }

    @Test
    void 아이디검색_존재하지않는아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(999);
        });
    }

    @Test
    void 계정아이디검색_존재하는계정아이디_펜션관리자반환() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager@pension.com");
        controller.save(requestDTO);

        // when
        PensionManager foundManager = controller.findByAccountId("manager1");

        // then
        assertNotNull(foundManager);
        assertEquals("이사장", foundManager.getName());
        assertEquals("manager1", foundManager.getAccountId());
    }

    @Test
    void 계정아이디검색_존재하지않는계정아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.findByAccountId("nonexistent");
        });
    }

    @Test
    void 아이디삭제_존재하는아이디_펜션관리자삭제() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager@pension.com");
        PensionManager savedManager = controller.save(requestDTO);

        // when
        controller.deleteById(savedManager.getId());

        // then
        List<PensionManager> allManagers = repository.findAll();
        assertTrue(allManagers.isEmpty());
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(savedManager.getId());
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
    void 전체조회_여러관리자존재_모든관리자반환() {
        // given
        PensionManagerRequestDTO manager1 = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager1@pension.com");
        PensionManagerRequestDTO manager2 = new PensionManagerRequestDTO(
                "김관리", "manager2", "admin456", "010-5555-2222", "manager2@pension.com");

        controller.save(manager1);
        controller.save(manager2);

        // when
        List<PensionManager> allManagers = controller.findAll();

        // then
        assertEquals(2, allManagers.size());
        assertEquals("이사장", allManagers.get(0).getName());
        assertEquals("김관리", allManagers.get(1).getName());
    }

    @Test
    void 로그인_올바른비밀번호_로그인성공() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager@pension.com");
        PensionManager savedManager = controller.save(requestDTO);

        // when
        boolean loginResult = savedManager.login("admin123");

        // then
        assertTrue(loginResult);
    }

    @Test
    void 로그인_잘못된비밀번호_로그인실패() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager@pension.com");
        PensionManager savedManager = controller.save(requestDTO);

        // when
        boolean loginResult = savedManager.login("wrongpassword");

        // then
        assertFalse(loginResult);
    }

    @Test
    void 프로필수정_유효한입력_프로필업데이트() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager@pension.com");
        PensionManager savedManager = controller.save(requestDTO);

        // when
        savedManager.editProfile("이사장(수정)", "010-9999-8888", "newmanager@pension.com");

        // then
        assertEquals("이사장(수정)", savedManager.getName());
        assertEquals("010-9999-8888", savedManager.getPhone());
        assertEquals("newmanager@pension.com", savedManager.getEmail());
    }

    @Test
    void 비밀번호재설정_새비밀번호_비밀번호업데이트() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager@pension.com");
        PensionManager savedManager = controller.save(requestDTO);

        // when
        savedManager.resetPassword("newadmin456");

        // then
        assertTrue(savedManager.login("newadmin456"));
        assertFalse(savedManager.login("admin123"));
    }

    @Test
    void 컨트롤러로그인_올바른계정정보_로그인성공() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager@pension.com");
        controller.save(requestDTO);

        // when
        PensionManager loginResult = controller.login("manager1", "admin123");

        // then
        assertNotNull(loginResult);
        assertEquals("이사장", loginResult.getName());
        assertEquals("manager1", loginResult.getAccountId());
    }

    @Test
    void 컨트롤러로그인_잘못된비밀번호_예외발생() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager@pension.com");
        controller.save(requestDTO);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            controller.login("manager1", "wrongpassword");
        });
    }

    @Test
    void 컨트롤러로그인_존재하지않는계정_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.login("nonexistent", "admin123");
        });
    }

    @Test
    void 서비스로그인_올바른계정정보_로그인성공() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "김관리", "manager2", "mypassword", "010-6666-7777", "manager2@pension.com");
        controller.save(requestDTO);
        PensionManagerService service = PensionManagerService.getInstance();

        // when
        PensionManager loginResult = service.login("manager2", "mypassword");

        // then
        assertNotNull(loginResult);
        assertEquals("김관리", loginResult.getName());
        assertEquals("manager2", loginResult.getAccountId());
    }

    @Test
    void 서비스로그인_잘못된비밀번호_예외발생() {
        // given
        PensionManagerRequestDTO requestDTO = new PensionManagerRequestDTO(
                "김관리", "manager2", "mypassword", "010-6666-7777", "manager2@pension.com");
        controller.save(requestDTO);
        PensionManagerService service = PensionManagerService.getInstance();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            service.login("manager2", "wrongpassword");
        });
    }

    @Test
    void 여러관리자중로그인_올바른계정정보_해당관리자로그인성공() {
        // given
        PensionManagerRequestDTO manager1 = new PensionManagerRequestDTO(
                "이사장", "manager1", "admin123", "010-5555-1111", "manager1@pension.com");
        PensionManagerRequestDTO manager2 = new PensionManagerRequestDTO(
                "김관리", "manager2", "admin456", "010-5555-2222", "manager2@pension.com");
        PensionManagerRequestDTO manager3 = new PensionManagerRequestDTO(
                "박부장", "manager3", "admin789", "010-5555-3333", "manager3@pension.com");

        controller.save(manager1);
        controller.save(manager2);
        controller.save(manager3);

        // when
        PensionManager loginResult = controller.login("manager2", "admin456");

        // then
        assertNotNull(loginResult);
        assertEquals("김관리", loginResult.getName());
        assertEquals("manager2", loginResult.getAccountId());
        assertEquals("010-5555-2222", loginResult.getPhone());
    }
}
