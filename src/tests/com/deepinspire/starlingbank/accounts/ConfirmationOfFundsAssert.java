package tests.com.deepinspire.starlingbank.accounts;

import main.com.deepinspire.starlingbank.beans.account.ConfirmationOfFundsBean;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ConfirmationOfFundsAssert extends AbstractAssert<ConfirmationOfFundsAssert, ConfirmationOfFundsBean> {
    public ConfirmationOfFundsAssert(ConfirmationOfFundsBean actual) {
        super(actual, ConfirmationOfFundsAssert.class);
    }

    public ConfirmationOfFundsAssert isValid() {
        isNotNull();

        Assertions.assertThat(actual.getRequestedAmountAvailableToSpend()).isNotNull();
        Assertions.assertThat(actual.getAccountWouldBeInOverdraftIfRequestedAmountSpent()).isNotNull();

        return this;
    }
	
	public static ConfirmationOfFundsAssert assertThat(ConfirmationOfFundsBean actual) {
        return new ConfirmationOfFundsAssert(actual);
    }
}
