package com.wynndie.spwallet.sharedCore.presentation.formatters

/**
 * Форматирует числовую строку в читаемый вид. Группирует разряды целой части
 * пробелом и разделяет целую и дробную часть с помощью [delimiter].
 *
 * Группировка целой части применяется только если она длиннее 4 символов.
 * Дробная часть выводится как есть, без группировки.
 *
 * @param delimiter разделитель между целой и дробной частью в результате
 * @return отформатированная строка со сгруппированной целой частью
 */
fun String.asFormattedAmount(delimiter: String = "."): String {
    val parts = this.split(",", ".", limit = 2)

    var whole = parts[0]
    if (whole.length > 4) whole = whole.replace(NUMBER_SEPARATOR, " ")
    if (parts.size <= 1) return whole

    val result = StringBuilder().append(whole.ifBlank { "0" })
    if (parts[1].isNotBlank()) result.append(delimiter).append(parts[1])

    return result.toString()
}

private val NUMBER_SEPARATOR = Regex("\\B(?=(\\d{3})+(?!\\d))")