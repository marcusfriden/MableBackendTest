fun interface TransactionValidation {
    fun validate(sender: Account, receiver: Account, transaction: Transaction): TransactionFailure?
}
