package account

interface AccountRepository {
    fun loadAccounts(): Map<AccountNumber, Account>
}
