package interfaces;

/**
 * An interface representing a wallet.
 */
public interface IAccount {
    /**
     * Sets the split percentage.
     * @param splitPercentage the split percentage.
     */
    void setSplitPercentage(double splitPercentage);

    /**
     * Sets the amount to transfer to this account.
     * @param amount the amount.
     */
    void setAmount(double amount);

    /**
     * Gets the address of this account.
     * @return the address.
     */
    String getAddress();

    /**
     * Gets the amount to  be transferred to this account.
     * @return the amount
     */
    double getAmount();

    /**
     * Gets if amount is already transferred to the address in this account.
     * @return true of amount is already transferred, false otherwise.
     */
    boolean getIsAmountTransferred();

    /**
     * Sets a value indicating whether amount is already transferred to the address in this account.
     * @param isAmountTransferred true of amount is already transferred, false otherwise.
     */
    void setIsAmountTransferred(boolean isAmountTransferred);
}
