import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TransactionLineParserTest {

    private val parser = TransactionLineParser()

    @Test
    fun `parses valid transaction line`() {
        val transaction = parser.parse("1111234522226789,1212343433335665,500.00")

        assertEquals(AccountNumber("1111234522226789"), transaction.from)
        assertEquals(AccountNumber("1212343433335665"), transaction.to)
        assertEquals(BigDecimal("500.00"), transaction.amount)
    }

    @Test
    fun `trims whitespace from fields`() {
        val transaction = parser.parse("  1111234522226789 , 1212343433335665 , 500.00 ")

        assertEquals(AccountNumber("1111234522226789"), transaction.from)
        assertEquals(AccountNumber("1212343433335665"), transaction.to)
        assertEquals(BigDecimal("500.00"), transaction.amount)
    }

    @Test
    fun `throws on wrong number of fields`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("not-a-valid-line")
        }
    }

    @Test
    fun `throws on invalid amount`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            parser.parse("1111234522226789,1212343433335665,xyz")
        }
        assertTrue(exception.message!!.contains("xyz"))
    }
}
