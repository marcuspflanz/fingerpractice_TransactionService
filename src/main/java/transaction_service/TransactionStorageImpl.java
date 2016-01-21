package transaction_service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Manages transaction instances.
 */
@Component
public final class TransactionStorageImpl implements TransactionStorage {
    /**
     * Transactions registered.
     */
    final TreeMap<Long, Transaction> transactions = new TreeMap<>();

    /**
     * Get a single transaction by ID.
     *
     * @param transactionID ID of transaction to obtain.
     * @return Desired Transaction or null if not found.
     */
    public Transaction getTransaction(long transactionID) {
        Long key = toKey(transactionID);

        return transactions.get(key);
    }

    /**
     * Register a transaction.
     *
     * @param transactionID    ID to use as key.
     * @param transactionToAdd Transaction to store.
     */
    public void addTransaction(long transactionID, Transaction transactionToAdd) {
        Long key = toKey(transactionID);

        // thread synch adding to avoid collisions with identical ID
        synchronized (transactions) {
            if (transactions.containsKey(transactionID)) {
                throw new Error(String.format("Transaction with key %s already exists.", transactionID));
            }

            transactions.put(key, transactionToAdd);
        }
    }

    /**
     * Get a group of transaction by their type.
     *
     * @param type Type of transactions to obtain.
     * @return Group of transactions.
     */
    public List<Long> getTransactionIDsByType(String type) {
        List<Long> transactionIDs = transactions.entrySet().stream()
                // filter types
                .filter(transactionToFilterPair -> {
                    String transactionType = transactionToFilterPair.getValue().getType();
                    boolean typesEqual = type.equals(transactionType);
                    return typesEqual;
                })
                // get id property
                .map(transactionToMapPair -> transactionToMapPair.getKey())
                // pipe into output
                .collect(Collectors.toList());

        return transactionIDs;
    }

    /**
     * Get a group of transaction by their parent transaction' ID.
     *
     * @param parentTransactionID ParentID of transactions to obtain.
     * @return Group of transactions.
     */
    public List<Transaction> getTransactionsByParent(long parentTransactionID) {
        List<Transaction> filteredTransactions = transactions.values().stream()
                // filter types
                .filter(transactionToFilter -> parentTransactionID == transactionToFilter.getParent_id())
                // pipe into output
                .collect(Collectors.toList());

        return filteredTransactions;
    }

    /**
     * Convert input to key type.
     *
     * @param valueToConvert Value to convert to string.
     * @return Key type.
     */
    private Long toKey(long valueToConvert) {
        return valueToConvert;
    }
}
