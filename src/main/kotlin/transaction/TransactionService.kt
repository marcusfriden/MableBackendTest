package transaction

import account.AccountRepository

class TransactionService(private val accountRepository: AccountRepository) {

    private val validations = listOf(
        validateSenderAccountExists,
        validateReceiverAccountExists,
        validateSufficientFunds
    )

    fun process(transactions: List<Transaction>): TransactionResult =
        transactions.fold(TransactionResult(accountRepository.loadAccounts(), emptyList())) { result, transaction ->
            val sender = result.accounts[transaction.from]
            val receiver = result.accounts[transaction.to]
            val failure = validations.firstNotNullOfOrNull { it.validate(sender, receiver, transaction) }
            if (failure != null) {
                result.copy(failures = result.failures + failure)
            } else {
                result.copy(
                    accounts = result.accounts
                        .plus(transaction.from to sender!!.copy(balance = sender.balance - transaction.amount))
                        .plus(transaction.to to receiver!!.copy(balance = receiver.balance + transaction.amount))
                )
            }
        }
}
