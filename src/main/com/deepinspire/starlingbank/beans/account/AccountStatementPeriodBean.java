package main.com.deepinspire.starlingbank.beans.account;

import java.util.Date;

/**
 * This class representing an account statement period by the API when calling
 * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts/{accountUid}/statement/available-periods
 */
public class AccountStatementPeriodBean implements java.io.Serializable {
    private String period;
    private Boolean partial;
	private Date endsAt;

    // Default constructor
    public AccountStatementPeriodBean() {
    }


    public String getPeriod() {
        return this.period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }


    public Boolean getPartial() {
        return this.partial;
    }

    public void setPartial(Boolean partial) {
        this.partial = partial;
    }
	
	
	public Date getEndsAt() {
        return this.endsAt;
    }

    public void setEndsAt(Date endsAt) {
        this.endsAt = endsAt;
    }
}

