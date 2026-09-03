package account

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InMemoryAccountRepositoryTest {

    @Test
    fun `loads accounts from provided map`() {
        val accountNumber = AccountNumber("1111234522226789")
        val account = Account(accountNumber, BigDecimal("5000.00"))
        val repository = InMemoryAccountRepository(mapOf(accountNumber to account))

        val accounts = repository.loadAll()

        assertEquals(1, accounts.size)
        assertEquals(BigDecimal("5000.00"), accounts[accountNumber]?.balance)
    }

    @Test
    fun `returns empty map when created with no accounts`() {
        val repository = InMemoryAccountRepository(emptyMap())

        assertTrue(repository.loadAll().isEmpty())
    }
}
