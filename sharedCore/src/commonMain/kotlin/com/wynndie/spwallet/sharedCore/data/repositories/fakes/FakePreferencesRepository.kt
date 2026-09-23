package com.wynndie.spwallet.sharedCore.data.repositories.fakes

import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePreferencesRepository : PreferencesRepository {

    private val selectedServer = MutableStateFlow(Servers.SP)

    override fun getSelectedServer(): Flow<Servers> {
        return selectedServer
    }

    override suspend fun setSelectedServer(server: Servers) {
        selectedServer.value = server
    }
}
