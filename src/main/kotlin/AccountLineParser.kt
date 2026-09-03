class AccountLineParser : CsvLineParser<Account> {

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
