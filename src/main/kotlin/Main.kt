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

        println("Updated Account Balances:")
        result.accounts.forEach { (accountNumber, account) ->
            println("  ${accountNumber.value}: ${account.balance}")
        }
        result.failures.printIfNotEmpty()
    } catch (e: Exception) {
        if (e is IllegalArgumentException) {
            println(e.message)
        } else {
            System.err.println("Error: ${e.message}")
        }
    }
}
