package account

/**
 * Inline class representing an AccountNumber String.
 */
@JvmInline
value class AccountNumber(val value: String) {
    /**
     * @throws IllegalArgumentException If the String value is not a valid account number.
     */
    init {
        require(value.length == 16 && value.all { it.isDigit() }) {
            "Account number must be exactly 16 digits, got: $value"
        }
    }
}
