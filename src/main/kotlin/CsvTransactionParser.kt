import java.io.BufferedReader
import java.io.Reader

class CsvTransactionParser(private val source: () -> Reader) : TransactionParser {

    override fun parse(): List<Transaction> =
        BufferedReader(source()).use { reader ->
            reader.lineSequence()
                .filter { it.isNotBlank() }
                .map { parseLine(it) }
                .toList()
        }

    private fun parseLine(line: String): Transaction {
        val parts = line.split(",")
        require(parts.size == 3) {
            "Invalid transaction line: expected 3 fields, got ${parts.size} in '$line'"
        }
        return Transaction(
            from = AccountNumber(parts[0].trim()),
            to = AccountNumber(parts[1].trim()),
            amount = parts[2].trim().toBigDecimalOrNull()
                ?: throw IllegalArgumentException("Invalid amount '${parts[2].trim()}' in '$line'")
        )
    }
}
