package main.com.deepinspire.starlingbank.beans.account;


/**
 * Currency and amount 
 */
public class CurrencyAndAmountBean implements java.io.Serializable {
    private String currency;
    private Integer minorUnits;

    // Default constructor
    public CurrencyAndAmountBean() {
    }


    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public Integer getMinorUnits() {
        return this.minorUnits;
    }

    public void setMinorUnits(Integer minorUnits) {
        this.minorUnits = minorUnits;
    }
	
	
	public String toString() {	
        return String.format("%s: %d", this.getCurrency(), this.getMinorUnits());
    }
}

