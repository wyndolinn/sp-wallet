package com.wynndie.spwallet.sharedCore.presentation.controllers.permission

import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException

object PermissionsController {

    private var permissionsController: PermissionsController? = null

    fun init(controller: PermissionsController) {
        if (permissionsController != null) return
        permissionsController = controller
    }

    suspend fun requestPermission(permission: Permission): Unit? {
        val controller = permissionsController ?: return null

        return try {
            when (controller.getPermissionState(permission)) {
                PermissionState.NotDetermined -> controller.openAppSettings()
                PermissionState.NotGranted -> controller.providePermission(permission)
                PermissionState.DeniedAlways -> controller.providePermission(permission)
                PermissionState.Denied -> controller.providePermission(permission)
                PermissionState.Granted -> controller.providePermission(permission)
            }
        } catch (_: DeniedAlwaysException) {

        } catch (_: DeniedException) {

        } catch(_: RequestCanceledException) {

        }
    }

    suspend fun getPermissionState(permission: Permission): PermissionState? {
        return permissionsController?.getPermissionState(permission)
    }
}