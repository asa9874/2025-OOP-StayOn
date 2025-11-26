package org.example.domain.user.customer;

import org.example.domain.user.User;

public class Customer extends User {
    private int money;

    public Customer(String name, String accountId, String password, String phone, String email) {
        super(name, accountId, password, phone, email);
        this.money = 1000000000;
    }

    public Customer(String name, String accountId, String password, String phone, String email, int money) {
        super(name, accountId, password, phone, email);
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public void subtractMoney(int amount) {
        if (this.money >= amount) {
            this.money -= amount;
        } else {
            throw new IllegalArgumentException("잔액이 부족합니다. 현재 잔액: " + this.money);
        }
    }
}
