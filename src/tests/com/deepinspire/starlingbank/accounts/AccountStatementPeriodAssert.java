package tests.com.deepinspire.starlingbank.accounts;

import main.com.deepinspire.starlingbank.beans.account.AccountStatementPeriodBean;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;


public class AccountStatementPeriodAssert extends AbstractAssert<AccountStatementPeriodAssert, AccountStatementPeriodBean> {
    public AccountStatementPeriodAssert(AccountStatementPeriodBean actual) {
        super(actual, AccountStatementPeriodAssert.class);
    }

    public AccountStatementPeriodAssert isValid() {
        isNotNull();

        Assertions.assertThat(actual.getPeriod()).isNotNull();
        Assertions.assertThat(actual.getPartial()).isNotNull();

        return this;
    }

    public static AccountStatementPeriodAssert assertThat(AccountStatementPeriodBean actual) {
        return new AccountStatementPeriodAssert(actual);
    }
}
