import account.Account
import account.AccountNumber
import account.AccountRepository
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TransactionServiceTest {

    private fun accounts(vararg pairs: Pair<String, String>): Map<AccountNumber, Account> =
        pairs.associate { (number, balance) ->
            val accountNumber = AccountNumber(number)
            accountNumber to Account(accountNumber, BigDecimal(balance))
        }

    private fun serviceWith(vararg pairs: Pair<String, String>): TransactionService =
        TransactionService(object : AccountRepository {
            override fun loadAccounts() = accounts(*pairs)
        })

    @Test
    fun `successful transaction debits sender and credits receiver`() {
        val service = serviceWith(
            "1111234522226789" to "5000.00",
            "1212343433335665" to "1200.00"
        )
        val transactions = listOf(
            Transaction(AccountNumber("1111234522226789"), AccountNumber("1212343433335665"), BigDecimal("500.00"))
        )

        val result = service.process(transactions)

        assertTrue(result.failures.isEmpty())
        assertEquals(BigDecimal("4500.00"), result.accounts[AccountNumber("1111234522226789")]?.balance)
        assertEquals(BigDecimal("1700.00"), result.accounts[AccountNumber("1212343433335665")]?.balance)
    }

    @Test
    fun `transaction that would overdraw account is rejected`() {
        val service = serviceWith(
            "1111234522226789" to "100.00",
            "1212343433335665" to "1200.00"
        )
        val transactions = listOf(
            Transaction(AccountNumber("1111234522226789"), AccountNumber("1212343433335665"), BigDecimal("500.00"))
        )

        val result = service.process(transactions)

        assertEquals(1, result.failures.size)
        assertTrue(result.failures[0] is TransactionFailure.InsufficientFunds)
        assertEquals(BigDecimal("100.00"), result.accounts[AccountNumber("1111234522226789")]?.balance)
        assertEquals(BigDecimal("1200.00"), result.accounts[AccountNumber("1212343433335665")]?.balance)
    }

    @Test
    fun `transaction of exact balance succeeds`() {
        val service = serviceWith(
            "1111234522226789" to "500.00",
            "1212343433335665" to "1200.00"
        )
        val transactions = listOf(
            Transaction(AccountNumber("1111234522226789"), AccountNumber("1212343433335665"), BigDecimal("500.00"))
        )

        val result = service.process(transactions)

        assertTrue(result.failures.isEmpty())
        assertEquals(BigDecimal("0.00"), result.accounts[AccountNumber("1111234522226789")]?.balance)
        assertEquals(BigDecimal("1700.00"), result.accounts[AccountNumber("1212343433335665")]?.balance)
    }

    @Test
    fun `multiple transactions are processed sequentially`() {
        val service = serviceWith(
            "1111234522226789" to "5000.00",
            "1212343433335665" to "1200.00",
            "3212343433335755" to "50000.00"
        )
        val transactions = listOf(
            Transaction(AccountNumber("1111234522226789"), AccountNumber("1212343433335665"), BigDecimal("500.00")),
            Transaction(AccountNumber("3212343433335755"), AccountNumber("1111234522226789"), BigDecimal("320.50"))
        )

        val result = service.process(transactions)

        assertTrue(result.failures.isEmpty())
        assertEquals(BigDecimal("4820.50"), result.accounts[AccountNumber("1111234522226789")]?.balance)
        assertEquals(BigDecimal("1700.00"), result.accounts[AccountNumber("1212343433335665")]?.balance)
        assertEquals(BigDecimal("49679.50"), result.accounts[AccountNumber("3212343433335755")]?.balance)
    }

    @Test
    fun `failed transaction does not prevent subsequent valid transactions`() {
        val service = serviceWith(
            "1111234522226789" to "100.00",
            "1212343433335665" to "1200.00",
            "3212343433335755" to "50000.00"
        )
        val transactions = listOf(
            Transaction(AccountNumber("1111234522226789"), AccountNumber("1212343433335665"), BigDecimal("500.00")),
            Transaction(AccountNumber("3212343433335755"), AccountNumber("1212343433335665"), BigDecimal("1000.00"))
        )

        val result = service.process(transactions)

        assertEquals(1, result.failures.size)
        assertTrue(result.failures[0] is TransactionFailure.InsufficientFunds)
        assertEquals(BigDecimal("100.00"), result.accounts[AccountNumber("1111234522226789")]?.balance)
        assertEquals(BigDecimal("2200.00"), result.accounts[AccountNumber("1212343433335665")]?.balance)
        assertEquals(BigDecimal("49000.00"), result.accounts[AccountNumber("3212343433335755")]?.balance)
    }

    @Test
    fun `throws when sender account not found`() {
        val service = serviceWith("1212343433335665" to "1200.00")
        val transactions = listOf(
            Transaction(AccountNumber("9999999999999999"), AccountNumber("1212343433335665"), BigDecimal("500.00"))
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            service.process(transactions)
        }
        assertTrue(exception.message!!.contains("9999999999999999"))
    }

    @Test
    fun `throws when receiver account not found`() {
        val service = serviceWith("1111234522226789" to "5000.00")
        val transactions = listOf(
            Transaction(AccountNumber("1111234522226789"), AccountNumber("9999999999999999"), BigDecimal("500.00"))
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            service.process(transactions)
        }
        assertTrue(exception.message!!.contains("9999999999999999"))
    }
}
