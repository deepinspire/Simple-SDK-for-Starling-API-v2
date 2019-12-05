package main.com.deepinspire.starlingbank.beans.account;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class representing an account balance returned by the API when calling
 * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts/{accountUid}/balance
 */
public class AccountBalanceBean implements java.io.Serializable {
    private CurrencyAndAmountBean clearedBalance;
    private CurrencyAndAmountBean effectiveBalance;
    private CurrencyAndAmountBean pendingTransactions;
    private CurrencyAndAmountBean availableToSpend;
    private CurrencyAndAmountBean acceptedOverdraft;
    private CurrencyAndAmountBean amount;
	

    // Default constructor
    public AccountBalanceBean() {
    }


    public CurrencyAndAmountBean getClearedBalance() {
        return this.clearedBalance;
    }

    public void setClearedBalance(CurrencyAndAmountBean clearedBalance) {
        this.clearedBalance = clearedBalance;
    }


    public CurrencyAndAmountBean getEffectiveBalance() {
        return this.effectiveBalance;
    }

    public void setEffectiveBalance(CurrencyAndAmountBean effectiveBalance) {
        this.effectiveBalance = effectiveBalance;
    }


    public CurrencyAndAmountBean getPendingTransactions() {
        return this.pendingTransactions;
    }

    public void setPendingTransactions(CurrencyAndAmountBean pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }


    public CurrencyAndAmountBean getAvailableToSpend() {
        return this.availableToSpend;
    }

    public void setAvailableToSpend(CurrencyAndAmountBean availableToSpend) {
        this.availableToSpend = availableToSpend;
    }
	

    public CurrencyAndAmountBean getAcceptedOverdraft() {
        return this.acceptedOverdraft;
    }

    public void setAcceptedOverdraft(CurrencyAndAmountBean acceptedOverdraft) {
        this.acceptedOverdraft = acceptedOverdraft;
    }
	

    public CurrencyAndAmountBean getAmount() {
        return this.amount;
    }

    public void setAmount(CurrencyAndAmountBean amount) {
        this.amount = amount;
    }
}
