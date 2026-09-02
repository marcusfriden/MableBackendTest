import java.io.BufferedReader
import java.io.Reader

class CsvAccountRepository(private val source: () -> Reader) : AccountRepository {

    override fun loadAccounts(): Map<AccountNumber, Account> =
        BufferedReader(source()).use { reader ->
            reader.lineSequence()
                .filter { it.isNotBlank() }
                .map { parseLine(it) }
                .associateBy { it.accountNumber }
        }

    private fun parseLine(line: String): Account {
        val parts = line.split(",")
        require(parts.size == 2) {
            "Invalid account line: expected 2 fields, got ${parts.size} in '$line'"
        }
        val accountNumber = AccountNumber(parts[0].trim())
        val balance = parts[1].trim().toBigDecimalOrNull()
            ?: throw IllegalArgumentException("Invalid balance '${parts[1].trim()}' in '$line'")
        return Account(accountNumber, balance)
    }
}
