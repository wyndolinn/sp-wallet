package com.wynndie.spwallet.sharedCore.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.models.SpServersOptions
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getSelectedSpServer(): Flow<SpServersOptions>
    suspend fun setSelectedSpServer(server: SpServersOptions)
}