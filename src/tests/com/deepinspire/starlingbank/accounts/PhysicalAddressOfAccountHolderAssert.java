package tests.com.deepinspire.starlingbank.accounts;

import main.com.deepinspire.starlingbank.beans.account.PhysicalAddressOfAccountHolderBean;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.time.Instant;
import java.util.Date;

public class PhysicalAddressOfAccountHolderAssert extends AbstractAssert<PhysicalAddressOfAccountHolderAssert, PhysicalAddressOfAccountHolderBean> {
    public PhysicalAddressOfAccountHolderAssert(PhysicalAddressOfAccountHolderBean actual) {
        super(actual, PhysicalAddressOfAccountHolderAssert.class);
    }

    public PhysicalAddressOfAccountHolderAssert isValid() {
        isNotNull();

        Assertions.assertThat(actual.getLine1()).isNotNull();
        Assertions.assertThat(actual.getLine2()).isNotNull();
        Assertions.assertThat(actual.getLine3()).isNotNull();
        Assertions.assertThat(actual.getPostTown()).isNotNull();
        Assertions.assertThat(actual.getPostCode()).isNotNull();
        Assertions.assertThat(actual.getCountryCode()).isNotNull();


        return this;
    }

    public static PhysicalAddressOfAccountHolderAssert assertThat(PhysicalAddressOfAccountHolderBean actual) {
        return new PhysicalAddressOfAccountHolderAssert(actual);
    }
}
