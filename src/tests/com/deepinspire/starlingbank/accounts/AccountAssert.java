package tests.com.deepinspire.starlingbank.accounts;

import main.com.deepinspire.starlingbank.beans.account.AccountBean;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.time.Instant;
import java.util.Date;

public class AccountAssert extends AbstractAssert<AccountAssert, AccountBean> {
    public AccountAssert(AccountBean actual) {
        super(actual, AccountAssert.class);
    }

    public AccountAssert isValid() {
        isNotNull();

        Assertions.assertThat(actual.getAccountUid()).isNotNull();
        Assertions.assertThat(actual.getDefaultCategory()).isNotNull();
        Assertions.assertThat(actual.getCurrency()).isNotNull();
        Assertions.assertThat(actual.getCreatedAt()).isBefore(Date.from(Instant.now()));

        return this;
    }

    public static AccountAssert assertThat(AccountBean actual) {
        return new AccountAssert(actual);
    }
}
