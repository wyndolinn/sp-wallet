package com.wynndie.spwallet.sharedCore.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.models.Servers
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getSelectedServer(): Flow<Servers>
    suspend fun setSelectedServer(server: Servers)
}