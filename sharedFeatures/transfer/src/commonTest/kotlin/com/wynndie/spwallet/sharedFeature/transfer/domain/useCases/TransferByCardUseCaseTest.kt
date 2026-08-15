package com.wynndie.spwallet.sharedFeature.transfer.domain.useCases

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.data.repositories.fakes.FakeCardsRepository
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyAuthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedFeature.transfer.data.repositories.fakes.FakeTransferRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class TransferByCardUseCaseTest {

    private lateinit var cardsRepository: FakeCardsRepository
    private lateinit var transferRepository: FakeTransferRepository
    private lateinit var useCase: TransferByCardUseCase

    @BeforeTest
    fun setUp() {
        cardsRepository = FakeCardsRepository()
        transferRepository = FakeTransferRepository()
        useCase = TransferByCardUseCase(transferRepository, cardsRepository)
    }

    @Test
    fun `invoke should update sender card balance when transfer is successful`() = runBlocking {
        val senderCard = emptyAuthedCard.copy(
            id = "senderId",
            authKey = "senderKey",
            balance = 1000L,
            number = "11111",
        )
        cardsRepository.insertAuthedCard(senderCard)

        transferRepository.makeTransactionResult = { authKey, receiver, amount, comment ->
            assertThat(authKey).isEqualTo("senderKey")
            assertThat(receiver).isEqualTo("22222")
            assertThat(amount).isEqualTo(200L)
            assertThat(comment).isEqualTo("Test comment")
            Outcome.Success(800L)
        }

        val result = useCase(
            card = senderCard,
            receiver = "22222",
            amount = "200",
            comment = "Test comment",
        )

        assertThat(result).isEqualTo(Outcome.Success(Unit))

        val updatedCards = cardsRepository.getAuthedCards().first()
        val expectedSenderCard = senderCard.copy(balance = 800L)
        assertThat(updatedCards).containsExactlyInAnyOrder(expectedSenderCard)
    }

    @Test
    fun `invoke should also update receiver card balance when transfer is between authed cards`() =
        runBlocking {
            val senderCard = emptyAuthedCard.copy(
                id = "senderId",
                authKey = "senderKey",
                balance = 1000L,
                number = "11111",
            )
            val receiverCard = emptyAuthedCard.copy(
                id = "receiverId",
                authKey = "receiverKey",
                balance = 300L,
                number = "22222",
            )

            cardsRepository.insertAuthedCard(senderCard)
            cardsRepository.insertAuthedCard(receiverCard)

            transferRepository.makeTransactionResult = { _, _, _, _ ->
                Outcome.Success(800L)
            }

            val result = useCase(
                card = senderCard,
                receiver = "22222",
                amount = "200",
                comment = "Test comment",
            )

            assertThat(result).isEqualTo(Outcome.Success(Unit))

            val updatedCards = cardsRepository.getAuthedCards().first()
            val expectedSenderCard = senderCard.copy(balance = 800L)
            val expectedReceiverCard = receiverCard.copy(balance = 500L)
            assertThat(updatedCards).containsExactlyInAnyOrder(
                expectedSenderCard,
                expectedReceiverCard,
            )
        }

    @Test
    fun `invoke should return error and not modify cards repository when makeTransaction fails`() =
        runBlocking {
            val senderCard = emptyAuthedCard.copy(
                id = "senderId",
                authKey = "senderKey",
                balance = 1000L,
                number = "11111",
            )
            cardsRepository.insertAuthedCard(senderCard)

            transferRepository.makeTransactionResult = { _, _, _, _ ->
                Outcome.Error(Error.Network.SERVER_ERROR)
            }

            val result = useCase(
                card = senderCard,
                receiver = "22222",
                amount = "200",
                comment = "Test comment",
            )

            assertThat(result).isEqualTo(Outcome.Error(Error.Network.SERVER_ERROR))

            val updatedCards = cardsRepository.getAuthedCards().first()
            assertThat(updatedCards).containsExactlyInAnyOrder(senderCard)
        }
}
