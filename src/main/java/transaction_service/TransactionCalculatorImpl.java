package transaction_service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides calculations for transactions.
 */
@Service
public final class TransactionCalculatorImpl implements TransactionCalculator {
    /**
     * Sum up all transaction amounts.
     * @param transactionsToSumUp Transactions to use for summing up.
     * @return Sum of transaction amounts.
     */
    public double sumTransactions(List<Transaction> transactionsToSumUp) {
        double sum = 0;

        for (Transaction transaction : transactionsToSumUp) {
            sum += transaction.getAmount();
        }

        return sum;
    }
}
