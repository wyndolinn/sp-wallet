package com.wynndie.spwallet.sharedCore.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppPrefsRepository {

    fun getLocalization(): Flow<String>
    fun setLocalization(languageIso: String)

    fun getColorScheme(): Flow<String>
    fun setColorScheme(schemeName: String)
}