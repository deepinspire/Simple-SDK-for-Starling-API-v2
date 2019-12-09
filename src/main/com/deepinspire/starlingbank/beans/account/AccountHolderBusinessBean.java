package main.com.deepinspire.starlingbank.beans.account;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class representing an account returned by the API when calling
 * https://developer.starlingbank.com/docs - Businesses - /api/v2/account-holder/business
 */
public class AccountHolderBusinessBean implements java.io.Serializable {
    private String companyName;
    private String companyRegistrationNumber;
    private String email;
    private String phone;

    // Default constructor
    public AccountHolderBusinessBean() {
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }

    public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
        this.companyRegistrationNumber = companyRegistrationNumber;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
