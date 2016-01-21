package transaction_service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * REST service endpoints as specified by code challenge doc.
 */
@RestController
@RequestMapping("/transactionservice")
public final class TransactionController {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public final class ResourceNotFoundException extends RuntimeException {
    }

    @Autowired
    TransactionStorage transactionStorage;

    @Autowired
    TransactionCalculator transactionCalculator;

    /**
     * Create a new transaction.
     *
     * Example request:
     * PUT /transactionservice/transaction/123
     * Body:
     * {
     *   "amount":double,
     *   "type":string,
     *   "parent_id":long
     * }
     *
     * @param id specifies a new transaction ID
     */
    @RequestMapping(
            value = "/transaction/{transaction_id}",
            method = RequestMethod.PUT
    )
    @ResponseStatus( HttpStatus.CREATED )
    public void addTransaction(
            @PathVariable(value="transaction_id") long id,
            @Valid @RequestBody Transaction newTransaction
    ) throws Exception {
        transactionStorage.addTransaction(id, newTransaction);
    }

    /**
     * Get a single transaction.
     *
     * Example request:
     * GET /transactionservice/transaction/123
     *
     * @param transactionID specifies transaction to get
     * @return Transaction for given ID.
     */
    @RequestMapping(
            value = "/transaction/{transaction_id}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public Transaction getTransaction(
            @PathVariable(value="transaction_id") long transactionID
    ) {
        Transaction transaction = transactionStorage.getTransaction(transactionID);
        if (transaction == null) {
            throw new ResourceNotFoundException();
        } else {
            return transaction;
        }
    }

    /**
     * Get all transactions with given type.
     *
     * Example request:
     * GET /transactionservice/types/myType
     *
     * @param type specifies type of transactions to get
     * @return Group of transactions for given criteria.
     */
    @RequestMapping(
            value = "/types/{type}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public List<Long> getTransactionsByType(
            @PathVariable(value="type") String type
    ) {
        return transactionStorage.getTransactionIDsByType(type);
    }


    /**
     * Get sum of amount of all transactions with given parent.
     * If no parent / null given will sum up root transactions (transactions without parent).
     *
     * Example request:
     * GET /transactionservice/sum/123
     *
     * @param parentTransactionID parent transaction id to sum up by
     * @return { "sum": double } A sum of all transactions that are transitively linked by their parent_id to $transaction_id
     */
    @RequestMapping(
            value = "/sum/{transaction_id}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public Object sumTransactionsByParent(
            @PathVariable(value="transaction_id") long parentTransactionID
    ) {
        List<Transaction> transactionsToSumUp = transactionStorage.getTransactionsByParent(parentTransactionID);
        double sum = transactionCalculator.sumTransactions(transactionsToSumUp);

        // if calculations can be flexibly combined in more complex responses we might implement a generic
        // object serializer but for a single resource this will do
        ObjectNode sumObject = JsonNodeFactory.instance.objectNode();
        sumObject.put("sum", sum);

        return sumObject;
    }
}