package com.wynndie.spwallet.sharedFeature.wallet.data.remote.model

import com.wynndie.spwallet.sharedFeature.wallet.domain.model.Transfer
import kotlinx.serialization.Serializable

@Serializable
data class TransferDto(
    val receiver: String,
    val amount: Long,
    val comment: String
) {
    companion object {
        fun from(transfer: Transfer): TransferDto {
            return TransferDto(
                receiver = transfer.receiverCardNumber,
                amount = transfer.amount,
                comment = transfer.comment
            )
        }
    }
}
