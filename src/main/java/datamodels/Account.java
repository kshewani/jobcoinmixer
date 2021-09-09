package datamodels;

import interfaces.IAccount;

/**
 * The class to hold destination account addresses and amount to transfer.
 */
public class Account implements IAccount {
    private String address;
    private double distributionPercentage;
    private double amount;
    private boolean isAmountTransferred;

    /**
     * Creates an account object.
     */
    public Account() { }

    /**
     * Creates an account object.
     * @param address the address of this account.
     * @param distributionPercentage indicates how much of total mixing request value is to be transferred to this account.
     */
    public Account(String address, double distributionPercentage) {
        this.address = address;
        this.distributionPercentage = distributionPercentage;
    }

    /**
     * Sets the distribution percentage.
     * @param distributionPercentage the distribution percentage.
     */
    @Override
    public void setDistributionPercentage(double distributionPercentage) {
        this.distributionPercentage = distributionPercentage;
    }

    /**
     * Sets the amount to transfer to this account.
     * @param amount the amount.
     */
    @Override
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gets the address of this account.
     * @return the address.
     */
    @Override
    public String getAddress() {
        return address;
    }

    /**
     * Gets the amount to  be transferred to this account.
     * @return the amount
     */
    @Override
    public double getAmount() {
        return amount;
    }

    /**
     * Gets if amount is already transferred to the address in this account.
     * @return true of amount is already transferred, false otherwise.
     */
    @Override
    public boolean getIsAmountTransferred() {
        return isAmountTransferred;
    }

    /**
     * Sets a value indicating whether amount is already transferred to the address in this account.
     * @param isAmountTransferred true of amount is already transferred, false otherwise.
     */
    @Override
    public void setIsAmountTransferred(boolean isAmountTransferred) {
        this.isAmountTransferred = isAmountTransferred;
    }
}
