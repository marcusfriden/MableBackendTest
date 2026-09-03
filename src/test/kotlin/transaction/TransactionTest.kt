package transaction

import account.AccountNumber
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class TransactionTest {

    @Test
    fun `creates transaction with from, to and amount`() {
        val transaction = Transaction(
            from = AccountNumber("1111234522226789"),
            to = AccountNumber("1212343433335665"),
            amount = BigDecimal("500.00")
        )
        assertEquals(AccountNumber("1111234522226789"), transaction.from)
        assertEquals(AccountNumber("1212343433335665"), transaction.to)
        assertEquals(BigDecimal("500.00"), transaction.amount)
    }

    @Test
    fun `two transactions with same fields are equal`() {
        val a = Transaction(AccountNumber("1111234522226789"), AccountNumber("1212343433335665"), BigDecimal("500.00"))
        val b = Transaction(AccountNumber("1111234522226789"), AccountNumber("1212343433335665"), BigDecimal("500.00"))
        assertEquals(a, b)
    }
}
