import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AccountNumberTest {

    @Test
    fun `creates account number from valid 16 digit string`() {
        val accountNumber = AccountNumber("1111234522226789")
        assertEquals("1111234522226789", accountNumber.value)
    }

    @Test
    fun `rejects account number shorter than 16 digits`() {
        assertFailsWith<IllegalArgumentException> {
            AccountNumber("123456789012345")
        }
    }

    @Test
    fun `rejects account number longer than 16 digits`() {
        assertFailsWith<IllegalArgumentException> {
            AccountNumber("12345678901234567")
        }
    }

    @Test
    fun `rejects account number with non-digit characters`() {
        assertFailsWith<IllegalArgumentException> {
            AccountNumber("111123452222678a")
        }
    }
}
