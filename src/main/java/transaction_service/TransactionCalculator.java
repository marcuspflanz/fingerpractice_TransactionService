package transaction_service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides calculations for transactions.
 */
@Service
public interface TransactionCalculator {
    /**
     * Sum up all transaction amounts.
     * @param transactionsToSumUp Transactions to use for summing up.
     * @return Sum of transaction amounts.
     */
    double sumTransactions(List<Transaction> transactionsToSumUp);
}
