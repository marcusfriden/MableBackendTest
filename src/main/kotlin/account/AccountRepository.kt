package account

interface AccountRepository {
    fun loadAll(): Map<AccountNumber, Account>
}
