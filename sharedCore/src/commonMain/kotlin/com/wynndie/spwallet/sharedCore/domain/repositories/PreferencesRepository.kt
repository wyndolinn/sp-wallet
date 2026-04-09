package com.wynndie.spwallet.sharedCore.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getSelectedSpServer(): Flow<SpServers>
    suspend fun setSelectedSpServer(server: SpServers)
}