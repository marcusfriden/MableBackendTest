import core.csv.CsvReader
import java.io.File

fun main(args: Array<String>) {
    require(args.size == 2) { "Usage: java -jar <application.jar> <balances.csv> <transactions.csv>" }

    val accounts = CsvReader({ File(args[0]).reader() }, AccountLineParser())
        .readAll()
        .associateBy { it.accountNumber }
    val accountRepository = InMemoryAccountRepository(accounts)
    val transactionService = TransactionService(accountRepository)

    val transactions = CsvReader({ File(args[1]).reader() }, TransactionLineParser()).readAll()
    val result = transactionService.process(transactions)

    println("Updated Account Balances:")
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
