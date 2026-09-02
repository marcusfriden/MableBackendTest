import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class AccountTest {

    @Test
    fun `creates account with account number and balance`() {
        val account = Account(
            accountNumber = AccountNumber("1111234522226789"),
            balance = BigDecimal("5000.00")
        )
        assertEquals(AccountNumber("1111234522226789"), account.accountNumber)
        assertEquals(BigDecimal("5000.00"), account.balance)
    }

    @Test
    fun `two accounts with same number and balance are equal`() {
        val a = Account(AccountNumber("1111234522226789"), BigDecimal("5000.00"))
        val b = Account(AccountNumber("1111234522226789"), BigDecimal("5000.00"))
        assertEquals(a, b)
    }
}
