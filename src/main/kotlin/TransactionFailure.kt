sealed class TransactionFailure(val transaction: Transaction) {
    class InsufficientFunds(transaction: Transaction) : TransactionFailure(transaction)
}
