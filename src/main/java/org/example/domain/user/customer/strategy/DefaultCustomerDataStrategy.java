package org.example.domain.user.customer.strategy;

import org.example.domain.user.customer.Customer;
import java.util.ArrayList;
import java.util.List;

public class DefaultCustomerDataStrategy implements CustomerInitStrategy {
    @Override
    public List<Customer> initializeList() {
        List<Customer> list = new ArrayList<>();
        
        Customer customer1 = new Customer("박민수", "customer1", "password123", "010-1111-2222", "minsu@example.com", 100000);
        customer1.setId(1);
        
        Customer customer2 = new Customer("이영희", "customer2", "password456", "010-3333-4444", "younghee@example.com", 50000);
        customer2.setId(2);
        
        list.add(customer1);
        list.add(customer2);
        
        return list;
    }
}
