package org.example.domain.user.customer.strategy;

import org.example.domain.user.customer.Customer;
import java.util.List;

public interface CustomerInitStrategy {
    List<Customer> initializeList();
}
