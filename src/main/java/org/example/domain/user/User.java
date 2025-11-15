package org.example.domain.user;

public class User {
    private int id;
    private String name;
    private String accountId;
    private String password;
    private String phone;
    private String email;

    public User(String name, String accountId, String password, String phone, String email) {
        this.name = name;
        this.accountId = accountId;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public void editProfile(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public void resetPassword(String newPassword) {
        this.password = newPassword;
    }
}
