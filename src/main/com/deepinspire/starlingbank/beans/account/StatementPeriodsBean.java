package main.com.deepinspire.starlingbank.beans.account;

import java.util.ArrayList;

/**
 * This class representing an response returned by the API when calling
 * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts/{accountUid}/statement/available-periods
 */
public class StatementPeriodsBean implements java.io.Serializable {
    private ArrayList<AccountStatementPeriodBean> periods = new ArrayList<>();

    // Default constructor
    public StatementPeriodsBean() {
    }

    public ArrayList<AccountStatementPeriodBean> getPeriods() {
        return periods;
    }

    public void setPeriods(ArrayList<AccountStatementPeriodBean> periods) {
        this.periods = periods;
    }
}
