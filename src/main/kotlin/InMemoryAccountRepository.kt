class InMemoryAccountRepository(private val accounts: Map<AccountNumber, Account>) : AccountRepository {

    override fun loadAccounts(): Map<AccountNumber, Account> = accounts
}
