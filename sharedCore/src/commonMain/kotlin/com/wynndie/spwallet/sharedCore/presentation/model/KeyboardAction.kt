package com.wynndie.spwallet.sharedCore.presentation.model

sealed interface KeyboardAction {

    data class OnClickNumber(val symbol: String) : KeyboardAction
    data class OnClickClearLast(val symbol: String) : KeyboardAction
    data class OnClickClearAll(val symbol: String) : KeyboardAction

    enum class OnClickOperation(val symbol: String) : KeyboardAction {
        ADD(symbol = "+"),
        SUBTRACT(symbol = "-"),
        MULTIPLY(symbol = "×"),
        DIVIDE(symbol = "÷")
    }

    enum class OnClickModifier(val symbol: String) : KeyboardAction {
        CALCULATE(symbol = "="),
        DECIMAL(symbol = "."),
        OPEN_PARENTHESES(symbol = "("),
        CLOSE_PARENTHESES(symbol = ")")
    }
}