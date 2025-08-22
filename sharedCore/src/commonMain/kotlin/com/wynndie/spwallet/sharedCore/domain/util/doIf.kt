package com.wynndie.spwallet.sharedCore.domain.util

inline fun <T> T.doIf(predicate: Boolean, action: T.() -> T): T {
    return if (predicate) action(this) else this
}

inline fun <T, R : Any> T.doIfNotNull(value: R?, action: T.(R) -> T): T {
    return if (value != null) action(this, value) else this
}