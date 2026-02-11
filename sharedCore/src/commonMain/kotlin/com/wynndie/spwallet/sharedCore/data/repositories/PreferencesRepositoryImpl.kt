package com.wynndie.spwallet.sharedCore.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wynndie.spwallet.sharedCore.domain.models.SpServersOptions
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(
    private val prefs: DataStore<Preferences>
) : PreferencesRepository {

    override fun getSelectedSpServer(): Flow<SpServersOptions> {
        val key = stringPreferencesKey("selected_sp_server")
        return prefs.data.map {
            val value = it[key] ?: SpServersOptions.SP.name
            SpServersOptions.valueOf(value)
        }
    }

    override suspend fun setSelectedSpServer(server: SpServersOptions) {
        prefs.edit {
            val key = stringPreferencesKey("selected_sp_server")
            it[key] = server.name
        }
    }
}