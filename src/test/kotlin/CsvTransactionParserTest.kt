import java.io.StringReader
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CsvTransactionParserTest {

    @Test
    fun `parses single transaction from csv`() {
        val parser = CsvTransactionParser { StringReader("1111234522226789,1212343433335665,500.00") }

        val transactions = parser.parse()

        assertEquals(1, transactions.size)
        val transaction = transactions[0]
        assertEquals(AccountNumber("1111234522226789"), transaction.from)
        assertEquals(AccountNumber("1212343433335665"), transaction.to)
        assertEquals(BigDecimal("500.00"), transaction.amount)
    }

    @Test
    fun `parses multiple transactions from csv`() {
        val csv = """
            1111234522226789,1212343433335665,500.00
            3212343433335755,2222123433331212,1000.00
            1111234522221234,1212343433335665,25.60
        """.trimIndent()
        val parser = CsvTransactionParser { StringReader(csv) }

        val transactions = parser.parse()

        assertEquals(3, transactions.size)
        assertEquals(BigDecimal("500.00"), transactions[0].amount)
        assertEquals(BigDecimal("1000.00"), transactions[1].amount)
        assertEquals(BigDecimal("25.60"), transactions[2].amount)
    }

    @Test
    fun `returns empty list for empty input`() {
        val parser = CsvTransactionParser { StringReader("") }

        val transactions = parser.parse()

        assertTrue(transactions.isEmpty())
    }

    @Test
    fun `skips blank lines`() {
        val csv = "1111234522226789,1212343433335665,500.00\n\n3212343433335755,2222123433331212,1000.00\n"
        val parser = CsvTransactionParser { StringReader(csv) }

        val transactions = parser.parse()

        assertEquals(2, transactions.size)
        assertEquals(AccountNumber("1111234522226789"), transactions[0].from)
        assertEquals(AccountNumber("1212343433335665"), transactions[0].to)
        assertEquals(BigDecimal("500.00"), transactions[0].amount)
        assertEquals(AccountNumber("3212343433335755"), transactions[1].from)
        assertEquals(AccountNumber("2222123433331212"), transactions[1].to)
        assertEquals(BigDecimal("1000.00"), transactions[1].amount)
    }

    @Test
    fun `throws on invalid csv line`() {
        val parser = CsvTransactionParser { StringReader("not-a-valid-line") }

        assertFailsWith<IllegalArgumentException> {
            parser.parse()
        }
    }
}
