package org.example.domain.user.customer;

import org.example.domain.user.User;

public class Customer extends User {
    public Customer(String name, String accountId, String password, String phone, String email) {
        super(name, accountId, password, phone, email);
    }
}
