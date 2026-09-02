class TransactionService {

    private val validations = listOf(
        TransactionValidation { sender, _, transaction ->
            if (sender.balance < transaction.amount) TransactionFailure.InsufficientFunds(transaction) else null
        }
    )

    fun process(
        accounts: Map<AccountNumber, Account>,
        transactions: List<Transaction>
    ): TransactionResult =
        transactions.fold(TransactionResult(accounts, emptyList())) { result, transaction ->
            val sender = requireAccount(result.accounts, transaction.from)
            val receiver = requireAccount(result.accounts, transaction.to)
            val failure = validations.firstNotNullOfOrNull { it.validate(sender, receiver, transaction) }

            if (failure != null) {
                result.copy(failures = result.failures + failure)
            } else {
                result.copy(
                    accounts = result.accounts
                        .plus(transaction.from to sender.copy(balance = sender.balance - transaction.amount))
                        .plus(transaction.to to receiver.copy(balance = receiver.balance + transaction.amount))
                )
            }
        }

    private fun requireAccount(accounts: Map<AccountNumber, Account>, accountNumber: AccountNumber): Account =
        accounts[accountNumber]
            ?: throw IllegalArgumentException("Account not found: ${accountNumber.value}")
}
