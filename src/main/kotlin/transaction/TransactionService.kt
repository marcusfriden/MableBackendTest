package transaction

import account.AccountRepository

/**
 * Service for transaction processing
 *
 * @property accountRepository A repository of available accounts
 */
class TransactionService(private val accountRepository: AccountRepository) {

    // This is a list of validations to be run for each transaction.
    private val validations = listOf(
        validateSenderAccountExists,
        validateReceiverAccountExists,
        validateSufficientFunds
    )

    /**
     * Validates and processes each transaction.
     *
     * We use a fold function (sometimes called accumulate) to process each transaction and combine them into
     * a final result containing accounts with their updated balances and a list of failures.
     *
     * @param transactions A list of transactions to process
     * @return The final TransactionResult
     */
    fun process(transactions: List<Transaction>): TransactionResult =
        transactions.fold(initial = TransactionResult(accountRepository.loadAll(), emptyList())) { result, transaction ->
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
