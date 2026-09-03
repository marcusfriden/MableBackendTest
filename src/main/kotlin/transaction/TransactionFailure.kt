package transaction

import transaction.TransactionFailure.InsufficientFunds
import transaction.TransactionFailure.ReceiverAccountNotFound
import transaction.TransactionFailure.SenderAccountNotFound

sealed class TransactionFailure(val transaction: Transaction) {
    class SenderAccountNotFound(transaction: Transaction) : TransactionFailure(transaction)
    class ReceiverAccountNotFound(transaction: Transaction) : TransactionFailure(transaction)
    class InsufficientFunds(transaction: Transaction) : TransactionFailure(transaction)
}

/**
 * Helper function that prints each failure in a list if it is not empty.
 */
fun List<TransactionFailure>.printIfNotEmpty() {
    if (isNotEmpty()) {
        println()
        println("Failed Transactions:")
        forEach { failure ->
            when (failure) {
                is SenderAccountNotFound ->
                    println("  SenderAccountNotFound: ${failure.transaction.from.value} -> ${failure.transaction.to.value}, amount: ${failure.transaction.amount}")
                is ReceiverAccountNotFound ->
                    println("  ReceiverAccountNotFound: ${failure.transaction.from.value} -> ${failure.transaction.to.value}, amount: ${failure.transaction.amount}")
                is InsufficientFunds ->
                    println("  InsufficientFunds: ${failure.transaction.from.value} -> ${failure.transaction.to.value}, amount: ${failure.transaction.amount}")
            }
        }
    }
}