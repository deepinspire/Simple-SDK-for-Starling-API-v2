package main.com.deepinspire.starlingbank.beans.account;

import java.util.ArrayList;

/**
 * This class representing an response returned by the API when calling
 * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts
 */
public class AccountsBean implements java.io.Serializable {
    private ArrayList<AccountBean> accounts = new ArrayList<>();

    // Default constructor
    public AccountsBean() {
    }

    public ArrayList<AccountBean> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<AccountBean> accounts) {
        this.accounts = accounts;
    }
}
