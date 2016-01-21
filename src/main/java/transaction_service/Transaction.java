package transaction_service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

/**
 * A financial transaction which may relate to a previously created parent transaction.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Transaction {
    // region Fields
    /**
     * Value moved by this transaction.
     */
    @NotNull
    double amount;

    /**
     * Type of transaction
     */
    @NotNull
    String type;

    /**
     * Eventually existing parent transaction this one relates to.
     */
    long parent_id;
    // endregion

    // region getters / setters
    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public long getParent_id() {
        return parent_id;
    }
    // endregion
}
