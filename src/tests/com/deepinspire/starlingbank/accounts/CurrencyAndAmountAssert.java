package tests.com.deepinspire.starlingbank.accounts;

import main.com.deepinspire.starlingbank.beans.account.CurrencyAndAmountBean;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class CurrencyAndAmountAssert extends AbstractAssert<CurrencyAndAmountAssert, CurrencyAndAmountBean> {
    public CurrencyAndAmountAssert(CurrencyAndAmountBean actual) {
        super(actual, CurrencyAndAmountAssert.class);
    }

    public CurrencyAndAmountAssert isValid() {
        isNotNull();

        Assertions.assertThat(actual.getCurrency()).isNotNull();
        Assertions.assertThat(actual.getMinorUnits()).isNotNull();

        return this;
    }
	
	public static CurrencyAndAmountAssert assertThat(CurrencyAndAmountBean actual) {
        return new CurrencyAndAmountAssert(actual);
    }
}
