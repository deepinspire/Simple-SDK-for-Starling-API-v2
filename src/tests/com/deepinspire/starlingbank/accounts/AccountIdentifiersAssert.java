package tests.com.deepinspire.starlingbank.accounts;

import main.com.deepinspire.starlingbank.beans.account.AccountIdentifiersBean;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.time.Instant;
import java.util.Date;

public class AccountIdentifiersAssert extends AbstractAssert<AccountIdentifiersAssert, AccountIdentifiersBean> {
    public AccountIdentifiersAssert(AccountIdentifiersBean actual) {
        super(actual, AccountIdentifiersAssert.class);
    }

    public AccountIdentifiersAssert isValid() {
        isNotNull();

        Assertions.assertThat(actual.getAccountIdentifier()).isNotNull();
        Assertions.assertThat(actual.getBankIdentifier()).isNotNull();
        Assertions.assertThat(actual.getIBAN()).isNotNull();
        Assertions.assertThat(actual.getBIC()).isNotNull();

        return this;
    }

    public static AccountIdentifiersAssert assertThat(AccountIdentifiersBean actual) {
        return new AccountIdentifiersAssert(actual);
    }
}
