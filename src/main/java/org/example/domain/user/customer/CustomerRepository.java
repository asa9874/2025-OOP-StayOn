package org.example.domain.user.customer;

import org.example.domain.user.customer.strategy.CustomerInitStrategy;
import java.util.List;
import java.util.Optional;

public class CustomerRepository {
    private static CustomerRepository instance;
    private List<Customer> customerList;
    private final CustomerInitStrategy initStrategy;
    private int nextId = 1;

    // 생성자
    private CustomerRepository(CustomerInitStrategy initStrategy) {
        this.initStrategy = initStrategy;
        this.customerList = initStrategy.initializeList();
        updateNextId();
    }

    // 다음 ID 업데이트 메서드
    private void updateNextId() {
        int maxId = customerList.stream()
                .mapToInt(Customer::getId)
                .max()
                .orElse(0);
        nextId = maxId + 1;
    }

    public static void initialize(CustomerInitStrategy initStrategy) {
        if (instance == null) {
            instance = new CustomerRepository(initStrategy);
        }
    }

    public static CustomerRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("CustomerRepository가 초기화되지 않았습니다. Init.initializeDependencies()를 먼저 호출하세요.");
        }
        return instance;
    }

    // 여기부터 CRUD 메서드
    public List<Customer> findAll() {
        return customerList;
    }

    public Optional<Customer> findById(int id) {
        return customerList.stream()
                .filter(customer -> customer.getId() == id)
                .findFirst();
    }

    public Optional<Customer> findByAccountId(String accountId) {
        return customerList.stream()
                .filter(customer -> customer.getAccountId().equals(accountId))
                .findFirst();
    }

    public Customer save(Customer customer) {
        customer.setId(nextId++);
        customerList.add(customer);
        return customer;
    }

    public void deleteById(int id) {
        customerList.removeIf(customer -> customer.getId() == id);
    }

    public void returnToDefaultData() {
        this.customerList = initStrategy.initializeList();
        updateNextId();
    }
}
