package account

/**
 * In-memory implementation of AccountRepository
 *
 * This is a very simple map-backed account repository. For this version it only contains one function which does not
 * really do anything other than wrap the map. But the idea is that it would be easy to add
 * other storage such as a DB without updating business logic.
 */
class InMemoryAccountRepository(private val accounts: Map<AccountNumber, Account>) : AccountRepository {

    override fun loadAll(): Map<AccountNumber, Account> = accounts
}
