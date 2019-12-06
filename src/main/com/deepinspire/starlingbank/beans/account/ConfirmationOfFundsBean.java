package main.com.deepinspire.starlingbank.beans.account;


/**
 * This class representing an account confirmation of funds returned by the API when calling
 * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts/{accountUid}/confirmation-of-funds
 */
public class ConfirmationOfFundsBean implements java.io.Serializable {
    private Boolean requestedAmountAvailableToSpend;
    private Boolean accountWouldBeInOverdraftIfRequestedAmountSpent;

    // Default constructor
    public ConfirmationOfFundsBean() {
    }


    public Boolean getRequestedAmountAvailableToSpend() {
        return this.requestedAmountAvailableToSpend;
    }

    public void setCurrency(Boolean requestedAmountAvailableToSpend) {
        this.requestedAmountAvailableToSpend = requestedAmountAvailableToSpend;
    }


    public Boolean getAccountWouldBeInOverdraftIfRequestedAmountSpent() {
        return this.accountWouldBeInOverdraftIfRequestedAmountSpent;
    }

    public void setAccountWouldBeInOverdraftIfRequestedAmountSpent(Boolean accountWouldBeInOverdraftIfRequestedAmountSpent) {
        this.accountWouldBeInOverdraftIfRequestedAmountSpent = accountWouldBeInOverdraftIfRequestedAmountSpent;
    }
}

