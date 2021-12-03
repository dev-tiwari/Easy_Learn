package com.example.easylearn;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class WithdrawalRequest {

    private String userId;
    private String phoneNumber;
    private String requestedBy;

    public WithdrawalRequest(String userId, String phoneNumber, String requestedBy) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.requestedBy = requestedBy;
    }

    public WithdrawalRequest(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    @ServerTimestamp
    private Date createdAt;


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
