package account

class InMemoryAccountRepository(private val accounts: Map<AccountNumber, Account>) : AccountRepository {

    override fun loadAll(): Map<AccountNumber, Account> = accounts
}
