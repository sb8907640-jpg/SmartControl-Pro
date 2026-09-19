package com.smartcontrol.owner.ui.adddevice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartcontrol.owner.data.model.InviteLink
import com.smartcontrol.owner.data.repository.LinkRepository
import com.smartcontrol.owner.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddDeviceUiState(
    val isLoading: Boolean = false,
    val selectedDeviceType: String? = null,
    val selectedPermissions: Set<String> = Constants.ALL_PERMISSIONS.map { it.id }.toSet(),
    val selectedExpiryHours: Int = 24,
    val generatedInvite: InviteLink? = null,
    val error: String? = null
)

@HiltViewModel
class AddDeviceViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddDeviceUiState())
    val uiState: StateFlow<AddDeviceUiState> = _uiState.asStateFlow()

    fun selectDeviceType(type: String?) {
        _uiState.value = _uiState.value.copy(selectedDeviceType = type)
    }

    fun togglePermission(permissionId: String) {
        val current = _uiState.value.selectedPermissions
        val updated = if (current.contains(permissionId)) {
            current - permissionId
        } else {
            current + permissionId
        }
        _uiState.value = _uiState.value.copy(selectedPermissions = updated)
    }

    fun selectAllPermissions() {
        _uiState.value = _uiState.value.copy(
            selectedPermissions = Constants.ALL_PERMISSIONS.map { it.id }.toSet()
        )
    }

    fun deselectAllPermissions() {
        _uiState.value = _uiState.value.copy(selectedPermissions = emptySet())
    }

    fun selectExpiry(hours: Int) {
        _uiState.value = _uiState.value.copy(selectedExpiryHours = hours)
    }

    fun generateInvite(onSuccess: (InviteLink) -> Unit) {
        if (_uiState.value.selectedPermissions.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                error = "Kam se kam ek permission select karein"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = linkRepository.generateInvite(
                deviceType = _uiState.value.selectedDeviceType,
                permissions = _uiState.value.selectedPermissions.toList(),
                expiryHours = _uiState.value.selectedExpiryHours
            )

            result.fold(
                onSuccess = { invite ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        generatedInvite = invite
                    )
                    onSuccess(invite)
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Invite generate nahi ho paya"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}