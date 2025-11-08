package org.example.domain.user.customer;

import org.example.Init;
import org.example.domain.user.customer.dto.CustomerRequestDTO;
import org.example.domain.user.customer.strategy.EmptyCustomerListStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    private CustomerController controller;
    private CustomerRepository repository;

    @BeforeEach
    void setUp() {
        Init.initializeCustomerModule(new EmptyCustomerListStrategy());
        repository = CustomerRepository.getInstance();
        controller = CustomerController.getInstance();
        repository.returnToDefaultData();
    }

    @Test
    void 저장_유효한입력_고객저장및반환() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);

        // when
        controller.save(requestDTO);

        // then
        List<Customer> allCustomers = repository.findAll();
        assertEquals(1, allCustomers.size());

        Customer customer = allCustomers.get(0);
        assertEquals("홍길동", customer.getName());
        assertEquals("hong123", customer.getAccountId());
        assertEquals("010-1234-5678", customer.getPhone());
        assertEquals("hong@example.com", customer.getEmail());
        assertEquals(10000, customer.getMoney());
        assertTrue(customer.getId() > 0);
    }

    @Test
    void 아이디검색_존재하는아이디_고객반환() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        Customer savedCustomer = controller.save(requestDTO);

        // when
        Customer foundCustomer = controller.findById(savedCustomer.getId());

        // then
        assertNotNull(foundCustomer);
        assertEquals("홍길동", foundCustomer.getName());
        assertEquals("hong123", foundCustomer.getAccountId());
        assertEquals("010-1234-5678", foundCustomer.getPhone());
        assertEquals("hong@example.com", foundCustomer.getEmail());
        assertEquals(10000, foundCustomer.getMoney());
    }

    @Test
    void 아이디검색_존재하지않는아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(999);
        });
    }

    @Test
    void 계정아이디검색_존재하는계정아이디_고객반환() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        controller.save(requestDTO);

        // when
        Customer foundCustomer = controller.findByAccountId("hong123");

        // then
        assertNotNull(foundCustomer);
        assertEquals("홍길동", foundCustomer.getName());
        assertEquals("hong123", foundCustomer.getAccountId());
    }

    @Test
    void 계정아이디검색_존재하지않는계정아이디_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.findByAccountId("nonexistent");
        });
    }

    @Test
    void 아이디삭제_존재하는아이디_고객삭제() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        Customer savedCustomer = controller.save(requestDTO);

        // when
        controller.deleteById(savedCustomer.getId());

        // then
        List<Customer> allCustomers = repository.findAll();
        assertTrue(allCustomers.isEmpty());
        assertThrows(NoSuchElementException.class, () -> {
            controller.findById(savedCustomer.getId());
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
    void 전체조회_여러고객존재_모든고객반환() {
        // given
        CustomerRequestDTO customer1 = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        CustomerRequestDTO customer2 = new CustomerRequestDTO(
                "김철수", "kim456", "password456", "010-8765-4321", "kim@example.com", 20000);

        controller.save(customer1);
        controller.save(customer2);

        // when
        List<Customer> allCustomers = controller.findAll();

        // then
        assertEquals(2, allCustomers.size());
        assertEquals("홍길동", allCustomers.get(0).getName());
        assertEquals("김철수", allCustomers.get(1).getName());
    }

    @Test
    void 로그인_올바른비밀번호_로그인성공() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        Customer savedCustomer = controller.save(requestDTO);

        // when
        boolean loginResult = savedCustomer.login("password123");

        // then
        assertTrue(loginResult);
    }

    @Test
    void 로그인_잘못된비밀번호_로그인실패() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        Customer savedCustomer = controller.save(requestDTO);

        // when
        boolean loginResult = savedCustomer.login("wrongpassword");

        // then
        assertFalse(loginResult);
    }

    @Test
    void 프로필수정_유효한입력_프로필업데이트() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        Customer savedCustomer = controller.save(requestDTO);

        // when
        savedCustomer.editProfile("홍길동(수정)", "010-9999-8888", "newemail@example.com");

        // then
        assertEquals("홍길동(수정)", savedCustomer.getName());
        assertEquals("010-9999-8888", savedCustomer.getPhone());
        assertEquals("newemail@example.com", savedCustomer.getEmail());
    }

    @Test
    void 비밀번호재설정_새비밀번호_비밀번호업데이트() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        Customer savedCustomer = controller.save(requestDTO);

        // when
        savedCustomer.resetPassword("newpassword456");

        // then
        assertTrue(savedCustomer.login("newpassword456"));
        assertFalse(savedCustomer.login("password123"));
    }

    @Test
    void 컨트롤러로그인_올바른계정정보_로그인성공() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        controller.save(requestDTO);

        // when
        Customer loginResult = controller.login("hong123", "password123");

        // then
        assertNotNull(loginResult);
        assertEquals("홍길동", loginResult.getName());
        assertEquals("hong123", loginResult.getAccountId());
    }

    @Test
    void 컨트롤러로그인_잘못된비밀번호_예외발생() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        controller.save(requestDTO);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            controller.login("hong123", "wrongpassword");
        });
    }

    @Test
    void 컨트롤러로그인_존재하지않는계정_예외발생() {
        // when & then
        assertThrows(NoSuchElementException.class, () -> {
            controller.login("nonexistent", "password123");
        });
    }

    @Test
    void 서비스로그인_올바른계정정보_로그인성공() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "김철수", "kim456", "mypassword", "010-5555-6666", "kim@example.com", 20000);
        controller.save(requestDTO);
        CustomerService service = CustomerService.getInstance();

        // when
        Customer loginResult = service.login("kim456", "mypassword");

        // then
        assertNotNull(loginResult);
        assertEquals("김철수", loginResult.getName());
        assertEquals("kim456", loginResult.getAccountId());
    }

    @Test
    void 서비스로그인_잘못된비밀번호_예외발생() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "김철수", "kim456", "mypassword", "010-5555-6666", "kim@example.com", 20000);
        controller.save(requestDTO);
        CustomerService service = CustomerService.getInstance();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            service.login("kim456", "wrongpassword");
        });
    }

    @Test
    void 여러고객중로그인_올바른계정정보_해당고객로그인성공() {
        // given
        CustomerRequestDTO customer1 = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        CustomerRequestDTO customer2 = new CustomerRequestDTO(
                "김철수", "kim456", "password456", "010-8765-4321", "kim@example.com", 20000);
        CustomerRequestDTO customer3 = new CustomerRequestDTO(
                "박영희", "park789", "password789", "010-1111-2222", "park@example.com", 30000);

        controller.save(customer1);
        controller.save(customer2);
        controller.save(customer3);

        // when
        Customer loginResult = controller.login("kim456", "password456");

        // then
        assertNotNull(loginResult);
        assertEquals("김철수", loginResult.getName());
        assertEquals("kim456", loginResult.getAccountId());
        assertEquals("010-8765-4321", loginResult.getPhone());
    }

    @Test
    void 잔액추가_유효한금액_잔액증가() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        Customer savedCustomer = controller.save(requestDTO);

        // when
        savedCustomer.addMoney(5000);

        // then
        assertEquals(15000, savedCustomer.getMoney());
    }

    @Test
    void 잔액차감_충분한잔액_잔액감소() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        Customer savedCustomer = controller.save(requestDTO);

        // when
        savedCustomer.subtractMoney(3000);

        // then
        assertEquals(7000, savedCustomer.getMoney());
    }

    @Test
    void 잔액차감_부족한잔액_예외발생() {
        // given
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(
                "홍길동", "hong123", "password123", "010-1234-5678", "hong@example.com", 10000);
        Customer savedCustomer = controller.save(requestDTO);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            savedCustomer.subtractMoney(15000);
        });
    }
}
