package com.wynndie.spwallet.sharedCore.domain.helpers

fun <T: Any> createModels(
    model: T,
    count: Int = 1,
    edit: (index: Int, model: T) -> T = { _, model -> model },
): List<T> {
    return (0..<count).map { index ->
        edit(index, model)
    }
}