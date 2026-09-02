data class TransactionResult(
    val accounts: Map<AccountNumber, Account>,
    val failures: List<TransactionFailure>
)
