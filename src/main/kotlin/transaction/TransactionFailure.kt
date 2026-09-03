package transaction

sealed class TransactionFailure(val transaction: Transaction) {
    class InsufficientFunds(transaction: Transaction) : TransactionFailure(transaction)
}

fun List<TransactionFailure>.printIfNotEmpty() {
    if (isNotEmpty()) {
        println()
        println("Failed Transactions:")
        forEach { failure ->
            when (failure) {
                is TransactionFailure.InsufficientFunds ->
                    println("  InsufficientFunds: ${failure.transaction.from.value} -> ${failure.transaction.to.value}, amount: ${failure.transaction.amount}")
            }
        }
    }
}