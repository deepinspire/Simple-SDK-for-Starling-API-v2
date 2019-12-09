package tests.com.deepinspire.starlingbank.accounts;

import main.com.deepinspire.starlingbank.beans.account.AccountHolderBusinessBean;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.time.Instant;
import java.util.Date;

public class AccountHolderBusinessAssert extends AbstractAssert<AccountHolderBusinessAssert, AccountHolderBusinessBean> {
    public AccountHolderBusinessAssert(AccountHolderBusinessBean actual) {
        super(actual, AccountHolderBusinessAssert.class);
    }

    public AccountHolderBusinessAssert isValid() {
        isNotNull();

        Assertions.assertThat(actual.getCompanyName()).isNotNull();
        Assertions.assertThat(actual.getCompanyRegistrationNumber()).isNotNull();
        Assertions.assertThat(actual.getEmail()).isNotNull();
        Assertions.assertThat(actual.getPhone()).isNotNull();

        return this;
    }

    public static AccountHolderBusinessAssert assertThat(AccountHolderBusinessBean actual) {
        return new AccountHolderBusinessAssert(actual);
    }
}
