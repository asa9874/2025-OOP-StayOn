package org.example.domain.user.customer.strategy;

import org.example.domain.user.customer.Customer;
import java.util.ArrayList;
import java.util.List;

public class EmptyCustomerListStrategy implements CustomerInitStrategy {
    @Override
    public List<Customer> initializeList() {
        return new ArrayList<>();
    }
}
