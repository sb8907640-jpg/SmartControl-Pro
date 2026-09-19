package com.smartcontrol.owner.ui.invite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartcontrol.owner.data.model.InviteLink
import com.smartcontrol.owner.data.repository.LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InviteUiState(
    val isLoading: Boolean = false,
    val invites: List<InviteLink> = emptyList(),
    val error: String? = null,
    val lastGenerated: InviteLink? = null
)

@HiltViewModel
class InviteViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InviteUiState())
    val uiState: StateFlow<InviteUiState> = _uiState.asStateFlow()

    init {
        loadInvites()
    }

    fun loadInvites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = linkRepository.getInviteLinks()
            result.fold(
                onSuccess = { invites ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        invites = invites
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun setLastGenerated(invite: InviteLink) {
        _uiState.value = _uiState.value.copy(lastGenerated = invite)
    }

    fun cancelInvite(id: String, onDone: () -> Unit) {
        viewModelScope.launch {
            val result = linkRepository.cancelInvite(id)
            result.fold(
                onSuccess = { onDone() },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
            )
        }
    }
}