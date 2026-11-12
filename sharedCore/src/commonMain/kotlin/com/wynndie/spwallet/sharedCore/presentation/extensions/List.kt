package com.wynndie.spwallet.sharedCore.presentation.extensions

inline fun <E> List<E>.ifNotEmpty(action: (List<E>) -> Unit) {
    if (this.isNotEmpty()) action(this) else return
}