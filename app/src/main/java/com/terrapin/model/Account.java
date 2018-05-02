package com.terrapin.model;

public class Account {

    private Long accountId;
    private String userName;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private boolean emailOptIn;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isEmailOptIn() {
        return emailOptIn;
    }

    public void setEmailOptIn(boolean emailOptIn) {
        this.emailOptIn = emailOptIn;
    }

    public Long getAccountId() {

        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
