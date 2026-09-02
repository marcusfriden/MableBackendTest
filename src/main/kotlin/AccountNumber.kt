@JvmInline
value class AccountNumber(val value: String) {
    init {
        require(value.length == 16 && value.all { it.isDigit() }) {
            "Account number must be exactly 16 digits, got: $value"
        }
    }
}
