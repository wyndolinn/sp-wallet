package com.wynndie.spwallet.sharedCore.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    fun getLocalization(): Flow<String>
    suspend fun setLocalization(languageIso: String)

    fun getColorScheme(): Flow<String>
    suspend fun setColorScheme(schemeName: String)
}