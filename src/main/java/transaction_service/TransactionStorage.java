package transaction_service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Manages transaction instances.
 */
@Service
public interface TransactionStorage {
    /**
     * Get a single transaction by ID.
     *
     * @param transactionID ID of transaction to obtain.
     * @return Desired Transaction or null if not found.
     */
    Transaction getTransaction(long transactionID);

    /**
     * Register a transaction.
     * @param transactionID    ID to use as key.
     * @param transactionToAdd Transaction to store.
     */
    void addTransaction(long transactionID, Transaction transactionToAdd) throws Exception;

    /**
     * Get a group of transaction by their type.
     *
     * @param type Type of transactions to obtain.
     * @return Group of transactions.
     */
    List<Long> getTransactionIDsByType(String type);

    /**
     * Get a group of transaction by their parent transaction' ID.
     *
     * @param parentTransactionID ParentID of transactions to obtain.
     * @return Group of transactions.
     */
    List<Transaction> getTransactionsByParent(long parentTransactionID);
}
