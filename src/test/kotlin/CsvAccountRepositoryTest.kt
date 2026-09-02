import java.io.StringReader
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CsvAccountRepositoryTest {

    @Test
    fun `loads single account from csv`() {
        val repository = CsvAccountRepository { StringReader("1111234522226789,5000.00") }

        val accounts = repository.loadAccounts()

        assertEquals(1, accounts.size)
        val account = accounts[AccountNumber("1111234522226789")]
        assertEquals(BigDecimal("5000.00"), account?.balance)
    }

    @Test
    fun `loads multiple accounts from csv`() {
        val csv = """
            1111234522226789,5000.00
            1111234522221234,10000.00
            2222123433331212,550.00
        """.trimIndent()
        val repository = CsvAccountRepository { StringReader(csv) }

        val accounts = repository.loadAccounts()

        assertEquals(3, accounts.size)
        assertEquals(BigDecimal("5000.00"), accounts[AccountNumber("1111234522226789")]?.balance)
        assertEquals(BigDecimal("10000.00"), accounts[AccountNumber("1111234522221234")]?.balance)
        assertEquals(BigDecimal("550.00"), accounts[AccountNumber("2222123433331212")]?.balance)
    }

    @Test
    fun `returns empty map for empty input`() {
        val repository = CsvAccountRepository { StringReader("") }

        val accounts = repository.loadAccounts()

        assertTrue(accounts.isEmpty())
    }

    @Test
    fun `skips blank lines`() {
        val csv = "1111234522226789,5000.00\n\n1111234522221234,10000.00\n"
        val repository = CsvAccountRepository { StringReader(csv) }

        val accounts = repository.loadAccounts()

        assertEquals(2, accounts.size)
        assertEquals(BigDecimal("5000.00"), accounts[AccountNumber("1111234522226789")]?.balance)
        assertEquals(BigDecimal("10000.00"), accounts[AccountNumber("1111234522221234")]?.balance)
    }

    @Test
    fun `re-reads source on each call`() {
        var callCount = 0
        val repository = CsvAccountRepository {
            callCount++
            StringReader("1111234522226789,${callCount}000.00")
        }

        val first = repository.loadAccounts()
        val second = repository.loadAccounts()

        assertEquals(BigDecimal("1000.00"), first[AccountNumber("1111234522226789")]?.balance)
        assertEquals(BigDecimal("2000.00"), second[AccountNumber("1111234522226789")]?.balance)
    }

    @Test
    fun `throws on invalid csv line`() {
        val repository = CsvAccountRepository { StringReader("not-a-valid-line") }

        assertFailsWith<IllegalArgumentException> {
            repository.loadAccounts()
        }
    }

    @Test
    fun `throws on invalid balance`() {
        val repository = CsvAccountRepository { StringReader("1111234522226789,abc") }

        val exception = assertFailsWith<IllegalArgumentException> {
            repository.loadAccounts()
        }
        assertTrue(exception.message!!.contains("abc"))
    }
}
