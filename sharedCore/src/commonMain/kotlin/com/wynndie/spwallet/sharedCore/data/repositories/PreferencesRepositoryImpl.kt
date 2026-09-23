package com.wynndie.spwallet.sharedCore.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(
    private val prefs: DataStore<Preferences>,
) : PreferencesRepository {

    override fun getSelectedServer(): Flow<Servers> {
        val key = stringPreferencesKey("selected_server")
        return prefs.data.map {
            val value = it[key] ?: Servers.SP.name
            Servers.valueOf(value)
        }
    }

    override suspend fun setSelectedServer(server: Servers) {
        prefs.edit {
            val key = stringPreferencesKey("selected_server")
            it[key] = server.name
        }
    }
}