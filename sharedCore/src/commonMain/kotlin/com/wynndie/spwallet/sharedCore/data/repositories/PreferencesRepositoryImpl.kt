package com.wynndie.spwallet.sharedCore.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(
    private val prefs: DataStore<Preferences>
) : PreferencesRepository {

    override fun getSelectedSpServer(): Flow<SpServers> {
        val key = stringPreferencesKey("selected_sp_server")
        return prefs.data.map {
            val value = it[key] ?: SpServers.SP.name
            SpServers.valueOf(value)
        }
    }

    override suspend fun setSelectedSpServer(server: SpServers) {
        prefs.edit {
            val key = stringPreferencesKey("selected_sp_server")
            it[key] = server.name
        }
    }
}