package main.com.deepinspire.starlingbank.beans.account;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class representing an physical address of account holder returned by the API when calling
 * https://developer.starlingbank.com/docs - Businesses - /api/v2/account-holder/business/correspondence-address
 */
public class PhysicalAddressOfAccountHolderBean implements java.io.Serializable {
    private String line1;
    private String line2;
    private String line3;
    private String postTown;
    private String postCode;
    private String countryCode;

    // Default constructor
    public PhysicalAddressOfAccountHolderBean() {
    }


    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }


    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }
	
	
	public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }


    public String getPostTown() {
        return postTown;
    }

    public void setPostTown(String postTown) {
        this.postTown = postTown;
    }


    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
