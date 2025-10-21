package com.wynndie.spwallet.sharedCore.presentation.controllers.permission

import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException

object PermissionsController {

    private var _controller: PermissionsController? = null

    fun init(controller: PermissionsController) {
        if (_controller != null) return
        _controller = controller
    }

    suspend fun requestPermission(permission: Permission): PermissionState {
        val controller = _controller ?: return PermissionState.NotDetermined

        return try {
            when (controller.getPermissionState(permission)) {
                PermissionState.NotDetermined -> controller.providePermission(permission)
                PermissionState.Granted -> controller.openAppSettings()
                PermissionState.NotGranted -> controller.openAppSettings()
                PermissionState.DeniedAlways -> controller.providePermission(permission)
                PermissionState.Denied -> controller.providePermission(permission)
            }
            controller.getPermissionState(permission)
        } catch (_: DeniedException) {
            controller.getPermissionState(permission)
        } catch (_: DeniedAlwaysException) {
            controller.openAppSettings()
            controller.getPermissionState(permission)
        } catch (_: RequestCanceledException) {
            controller.getPermissionState(permission)
        }
    }

    suspend fun getPermissionState(permission: Permission): PermissionState {
        return _controller
            ?.getPermissionState(permission)
            ?: PermissionState.NotDetermined
    }
}