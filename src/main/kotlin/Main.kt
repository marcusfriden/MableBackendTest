import java.io.File

fun main(args: Array<String>) {
    require(args.size == 2) { "Usage: mable-banking <balances.csv> <transactions.csv>" }

    val accountRepository = CsvAccountRepository { File(args[0]).reader() }
    val transactionParser = CsvTransactionParser { File(args[1]).reader() }
    val transactionService = TransactionService(accountRepository)

    val transactions = transactionParser.parse()
    val result = transactionService.process(transactions)

    println("Account Balances:")
    result.accounts.forEach { (accountNumber, account) ->
        println("  ${accountNumber.value}: ${account.balance}")
    }

    if (result.failures.isNotEmpty()) {
        println()
        println("Failed Transactions:")
        result.failures.forEach { failure ->
            when (failure) {
                is TransactionFailure.InsufficientFunds ->
                    println("  InsufficientFunds: ${failure.transaction.from.value} -> ${failure.transaction.to.value}, amount: ${failure.transaction.amount}")
            }
        }
    }
}
