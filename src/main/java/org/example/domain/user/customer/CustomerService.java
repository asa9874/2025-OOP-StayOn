package org.example.domain.user.customer;

import org.example.domain.user.customer.dto.CustomerRequestDTO;
import java.util.List;
import java.util.NoSuchElementException;

public class CustomerService {
    private static CustomerService instance;
    private final CustomerRepository customerRepository;

    private CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void initialize(CustomerRepository customerRepository) {
        if (instance == null) {
            instance = new CustomerService(customerRepository);
        }
    }

    public static CustomerService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("CustomerService가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    public Customer findById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + id + "인 고객을 찾을 수 없습니다."));
    }

    public Customer findByAccountId(String accountId) {
        return customerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NoSuchElementException("계정 ID가 " + accountId + "인 고객을 찾을 수 없습니다."));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer save(CustomerRequestDTO requestDTO) {
        Customer newCustomer = new Customer(
                requestDTO.name(),
                requestDTO.accountId(),
                requestDTO.password(),
                requestDTO.phone(),
                requestDTO.email(),
                requestDTO.money()
        );
        return customerRepository.save(newCustomer);
    }

    public void deleteById(int id) {
        findById(id);
        customerRepository.deleteById(id);
    }

    public Customer login(String accountId, String password) {
        Customer customer = customerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NoSuchElementException("계정 ID가 " + accountId + "인 고객을 찾을 수 없습니다."));
        
        if (!customer.login(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        
        return customer;
    }
}
