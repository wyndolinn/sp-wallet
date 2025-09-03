package com.wynndie.spwallet.sharedFeature.profile.presentation.screen.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.repository.UserRepository
import com.wynndie.spwallet.sharedCore.presentation.model.emptyAuthedUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.core.option.viewModelScopeFactory

class MenuViewModel(
    userRepository: UserRepository,
    private val args: MenuViewModelArgs
) : ViewModel() {

    private val _state = MutableStateFlow(MenuState())
    val state = _state.asStateFlow()

    init {
        userRepository.getAuthedUsers().onEach { users ->
            _state.update {
                it.copy(user = users.firstOrNull() ?: emptyAuthedUser)
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: MenuAction) {
        when (action) {
            MenuAction.OnClickBack -> {
                args.onClickBack()
            }

            MenuAction.OnClickTheme -> {
                args.onClickTheme()
            }
        }
    }
}