package org.example.domain.user.customer;

import org.example.domain.user.customer.dto.CustomerRequestDTO;
import java.util.List;

public class CustomerController {
    private static CustomerController instance;
    private final CustomerService customerService;
    
    private CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    public static void initialize(CustomerService customerService) {
        if (instance == null) {
            instance = new CustomerController(customerService);
        }
    }

    public static CustomerController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("CustomerController가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public Customer findById(int id) {
        return customerService.findById(id);
    }

    public Customer findByAccountId(String accountId) {
        return customerService.findByAccountId(accountId);
    }

    public Customer save(CustomerRequestDTO requestDTO) {
        return customerService.save(requestDTO);
    }

    public List<Customer> findAll() {
        return customerService.findAll();
    }

    public void deleteById(int id) {
        customerService.deleteById(id);
    }

    public Customer login(String accountId, String password) {
        return customerService.login(accountId, password);
    }
}
