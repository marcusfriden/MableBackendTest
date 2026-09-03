package account

import core.csv.CsvLineParser

/**
 * Account implementation of the CsvLineParser functional interface
 */
class AccountLineParser : CsvLineParser<Account> {

    /**
     * Parses a CSV line to an Account
     *
     * @param line A comma separated string
     * @return An Account corresponding to the csv
     * @throws IllegalArgumentException If the line has an incorrect number of fields or if either value is malformed
     */
    override fun parse(line: String): Account {
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
