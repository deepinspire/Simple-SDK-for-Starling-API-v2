package main.com.deepinspire.starlingbank.beans.account;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class representing an account returned by the API when calling
 * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts
 */
public class AccountBean implements java.io.Serializable {
    private String accountUid;
    private String defaultCategory;
    private String currency;
    private Date createdAt;

    // Default constructor
    public AccountBean() {
    }


    public String getAccountUid() {
        return accountUid;
    }

    public void setAccountUid(String accountUid) {
        this.accountUid = accountUid;
    }


    public String getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(String defaultCategory) {
        this.defaultCategory = defaultCategory;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
