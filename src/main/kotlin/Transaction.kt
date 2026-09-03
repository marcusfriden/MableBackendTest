import account.AccountNumber
import java.math.BigDecimal


data class Transaction(val from: AccountNumber, val to: AccountNumber, val amount: BigDecimal)
