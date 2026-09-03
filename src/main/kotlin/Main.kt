import account.AccountLineParser
import account.InMemoryAccountRepository
import core.csv.CsvReader
import transaction.TransactionLineParser
import transaction.TransactionService
import transaction.printIfNotEmpty
import java.io.File

fun main(args: Array<String>) {
    try {
        require(args.size == 2) { "Usage: java -jar <application.jar> <balances.csv> <transactions.csv>" }

        val accounts = CsvReader({ File(args[0]).reader() }, AccountLineParser())
            .readAll()
            .associateBy { it.accountNumber }
        val accountRepository = InMemoryAccountRepository(accounts)
        val transactionService = TransactionService(accountRepository)

        val transactions = CsvReader({ File(args[1]).reader() }, TransactionLineParser()).readAll()

        val result = transactionService.process(transactions)
        println("Processed ${transactions.size - result.failures.size}/${transactions.size} transactions\n")

        println("Account Balances:")
        result.accounts.forEach { (accountNumber, account) ->
            println("  ${accountNumber.value}: ${account.balance}")
        }
        result.failures.printIfNotEmpty()
    } catch (e: Exception) {
        // This would be a good place to log the full stack trace. For this version we will just print the messages.
        if (e is IllegalArgumentException) {
            // Bad input that the user has to fix. A custom exception would have been better in a real product.
            println(e.message)
        } else {
            System.err.println("Error: ${e.message}")
        }
    }
}
