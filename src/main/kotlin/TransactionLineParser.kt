import account.AccountNumber
import core.csv.CsvLineParser

class TransactionLineParser : CsvLineParser<Transaction> {

    override fun parse(line: String): Transaction {
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
