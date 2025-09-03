package com.wynndie.spwallet.sharedCore.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wynndie.spwallet.sharedCore.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(
    val dataStore: DataStore<Preferences>
) : DataStoreRepository {
    override fun getLocalization(): Flow<String> {
        val languageIsoKey = stringPreferencesKey("languageIso")
        return dataStore.data.map {
            it[languageIsoKey] ?: ""
        }
    }

    override suspend fun setLocalization(languageIso: String) {
        val languageIsoKey = stringPreferencesKey("languageIso")
        dataStore.edit {
            it[languageIsoKey] = languageIso
        }
    }


    override fun getColorScheme(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun setColorScheme(schemeName: String) {
        TODO("Not yet implemented")
    }

}