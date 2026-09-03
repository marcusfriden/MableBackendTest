package transaction

import account.Account

/**
 * A functional interface used to create transaction validation rules
 */
fun interface TransactionValidation {
    fun validate(sender: Account?, receiver: Account?, transaction: Transaction): TransactionFailure?
}

val validateSenderAccountExists = TransactionValidation { sender, _, transaction ->
    if (sender == null) TransactionFailure.SenderAccountNotFound(transaction) else null
}

val validateReceiverAccountExists = TransactionValidation { _, receiver, transaction ->
    if (receiver == null) TransactionFailure.ReceiverAccountNotFound(transaction) else null
}

val validateSufficientFunds = TransactionValidation { sender, _, transaction ->
    if (sender != null && sender.balance < transaction.amount) TransactionFailure.InsufficientFunds(transaction) else null
}
