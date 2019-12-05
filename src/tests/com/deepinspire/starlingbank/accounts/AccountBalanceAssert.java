package tests.com.deepinspire.starlingbank.accounts;

import main.com.deepinspire.starlingbank.beans.account.AccountBalanceBean;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;


public class AccountBalanceAssert extends AbstractAssert<AccountBalanceAssert, AccountBalanceBean> {
    public AccountBalanceAssert(AccountBalanceBean actual) {
        super(actual, AccountBalanceAssert.class);
    }

    public AccountBalanceAssert isValid() {
        isNotNull();

        CurrencyAndAmountAssert.assertThat(actual.getClearedBalance()).isValid();
        CurrencyAndAmountAssert.assertThat(actual.getEffectiveBalance()).isValid();
        CurrencyAndAmountAssert.assertThat(actual.getPendingTransactions()).isValid();
        CurrencyAndAmountAssert.assertThat(actual.getAvailableToSpend()).isValid();
        CurrencyAndAmountAssert.assertThat(actual.getAcceptedOverdraft()).isValid();
        CurrencyAndAmountAssert.assertThat(actual.getAmount()).isValid();

        return this;
    }

    public static AccountBalanceAssert assertThat(AccountBalanceBean actual) {
        return new AccountBalanceAssert(actual);
    }
}
