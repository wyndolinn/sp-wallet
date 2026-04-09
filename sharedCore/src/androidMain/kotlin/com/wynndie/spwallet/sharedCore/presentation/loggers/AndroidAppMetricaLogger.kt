package com.wynndie.spwallet.sharedCore.presentation.loggers

import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.profile.Attribute
import io.appmetrica.analytics.profile.UserProfile

class AndroidAppMetricaLogger : Logger {
//    override fun reportEnterScreen(screen: String) {
//        AppMetrica.reportEvent(Events.SCREEN_ENTER, mapOf<String, Any>("screen" to screen))
//    }
//
//    override fun reportNewCard(
//        userName: String,
//        cardNumber: String,
//        server: SpServers
//    ) {
//        TODO("Not yet implemented")
//    }
//
//    override fun reportCardDelete(
//        userName: String,
//        cardNumber: String,
//        servers: SpServers
//    ) {
//        TODO("Not yet implemented")
//    }
//
//    override fun reportTransferSuccess(
//        userName: String,
//        userCardNumber: String,
//        recipientCardNumber: String,
//        server: SpServers
//    ) {
//        TODO("Not yet implemented")
//    }
//
//    override fun reportUser(id: String, name: String, server: SpServers) {
//        val userProfile = UserProfile.newBuilder()
//            .apply(Attribute.customString("id").withValue(id))
//            .apply(Attribute.customString("server").withValue(server.name))
//            .apply(Attribute.name().withValue(name))
//            .build()
//    }
//
//    override fun reportEvent(event: Events) {
//        AppMetrica.reportEvent(event.name)
//    }
//
//    override fun reportEvent(event: Events, attributes: Map<String, Any>) {
//        AppMetrica.reportEvent(event.name, attributes)
//    }
}