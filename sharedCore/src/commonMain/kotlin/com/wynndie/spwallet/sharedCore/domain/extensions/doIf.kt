package com.wynndie.spwallet.sharedCore.domain.extensions

inline fun <T> T.doIf(predicate: Boolean, action: T.() -> T): T {
    return if (predicate) action(this) else this
}

inline fun <T, R> T.doIfNull(value: R?, action: T.() -> T): T {
    return if (value == null) action(this) else this
}

inline fun <T, R> T.doIfNotNull(value: R?, action: T.(R) -> T): T {
    return if (value != null) action(this, value) else this
}