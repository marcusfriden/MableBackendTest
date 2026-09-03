package account

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AccountLineParserTest {

    private val parser = AccountLineParser()

    @Test
    fun `parses valid account line`() {
        val account = parser.parse("1111234522226789,5000.00")

        assertEquals(AccountNumber("1111234522226789"), account.accountNumber)
        assertEquals(BigDecimal("5000.00"), account.balance)
    }

    @Test
    fun `trims whitespace from fields`() {
        val account = parser.parse("  1111234522226789 , 5000.00 ")

        assertEquals(AccountNumber("1111234522226789"), account.accountNumber)
        assertEquals(BigDecimal("5000.00"), account.balance)
    }

    @Test
    fun `throws on wrong number of fields`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("not-a-valid-line")
        }
    }

    @Test
    fun `throws on invalid balance`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            parser.parse("1111234522226789,abc")
        }
        assertTrue(exception.message!!.contains("abc"))
    }
}
