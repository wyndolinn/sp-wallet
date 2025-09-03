package com.wynndie.spwallet.sharedCore.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DataStoreFactory {

    fun create(): DataStore<Preferences>
}