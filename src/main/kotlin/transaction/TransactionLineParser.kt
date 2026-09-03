package transaction

import account.AccountNumber
import core.csv.CsvLineParser

/**
 * Transaction implementation of the CsvLineParser functional interface
 */
class TransactionLineParser : CsvLineParser<Transaction> {

    /**
     * Parses a CSV line to a Transaction
     *
     * @param line A comma separated string
     * @return A Transaction corresponding to the csv
     * @throws IllegalArgumentException If the line has an incorrect number of fields or if any value is malformed
     */
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
