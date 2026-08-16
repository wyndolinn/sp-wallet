package com.wynndie.spwallet.sharedCore.presentation.formatters

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.x_of_ore
import com.wynndie.spwallet.sharedCore.x_of_shulkers
import com.wynndie.spwallet.sharedCore.x_of_stacks

/**
 * Представление количества руды в шалкерах, стаках и отдельных единицах руды.
 *
 * @property value исходное количество единиц руды
 * @property formatted список текстовых блоков для отображения
 */
data class DisplayableOreValue(
    val value: Long,
    val formatted: List<UiText>
) {

    @Composable
    fun asString(): String {
        // Предупреждение игнорируется, потому что map
        // сохраняет Composable контекст внутри лямбды,
        // в отличии от .jointToString() { it.asString() }
        return formatted.map { it.asString() }.joinToString(" ")
    }

    companion object {
        private const val ORE_IN_STACK = 64
        private const val STACKS_IN_SHULKER = 27

        /**
         * Создаёт [DisplayableOreValue] для [value], раскладывая его
         * на шалкеры, стопки и единицы руды.
         *
         * Если [value] меньше размера одного стака, то [formatted] вернёт пустой список,
         * несмотря на ненулевое значение. В остальном,
         * каждый компонент (шалкера/стаки/руда) попадает в [formatted], только если он больше нуля.
         *
         * @param value количество единиц руды
         * @return [DisplayableOreValue] с готовым для отображения разложением
         */
        fun of(value: Long): DisplayableOreValue {
            if (value == 0L) {
                return DisplayableOreValue(
                    value = value,
                    formatted = emptyList()
                )
            }

            val shulkers = value / ORE_IN_STACK / STACKS_IN_SHULKER
            val stacks = value / ORE_IN_STACK % STACKS_IN_SHULKER
            val ore = value % ORE_IN_STACK

            val formattedValue = if (value >= ORE_IN_STACK) {
                buildList {
                    if (shulkers > 0) add(UiText.ResourceString(Res.string.x_of_shulkers, shulkers))
                    if (stacks > 0) add(UiText.ResourceString(Res.string.x_of_stacks, stacks))
                    if (ore > 0) add(UiText.ResourceString(Res.string.x_of_ore, ore))
                }
            } else emptyList()

            return DisplayableOreValue(
                value = value,
                formatted = formattedValue
            )
        }
    }
}