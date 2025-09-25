package com.wynndie.spwallet.sharedFeature.transfer.data.remote.dto

import com.wynndie.spwallet.sharedFeature.transfer.domain.models.Transfer
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
