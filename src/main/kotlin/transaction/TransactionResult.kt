package transaction

import account.Account
import account.AccountNumber

data class TransactionResult(
    val accounts: Map<AccountNumber, Account>,
    val failures: List<TransactionFailure>
)
