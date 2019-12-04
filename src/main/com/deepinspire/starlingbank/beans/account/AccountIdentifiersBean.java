package main.com.deepinspire.starlingbank.beans.account;


/**
 * This class representing an account identifiers returned by the API when calling
 * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts/{accountUid}/identifiers
 */
public class AccountIdentifiersBean implements java.io.Serializable {
    private String accountIdentifier;
    private String bankIdentifier;
    private String iban;
    private String bic;

    // Default constructor
    public AccountIdentifiersBean() {
    }


    public String getAccountIdentifier() {
        return this.accountIdentifier;
    }

    public void setAccountUid(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }


    public String getBankIdentifier() {
        return this.bankIdentifier;
    }

    public void setBankIdentifier(String bankIdentifier) {
        this.bankIdentifier = bankIdentifier;
    }


    public String getIBAN() {
        return this.iban;
    }

    public void setIBAN(String iban) {
        this.iban = iban;
    }


    public String getBIC() {
        return this.bic;
    }

    public void setBIC(String bic) {
        this.bic = bic;
    }
}
