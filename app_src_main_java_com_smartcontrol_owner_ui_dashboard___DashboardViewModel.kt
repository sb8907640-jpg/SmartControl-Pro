package com.smartcontrol.owner.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartcontrol.owner.data.model.Device
import com.smartcontrol.owner.data.repository.AuthRepository
import com.smartcontrol.owner.data.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = false,
    val devices: List<Device> = emptyList(),
    val error: String? = null,
    val totalDevices: Int = 0,
    val onlineDevices: Int = 0,
    val totalAlerts: Int = 0
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDevices()
    }

    fun loadDevices() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = deviceRepository.getDevices()
            result.fold(
                onSuccess = { devices ->
                    val online = devices.count { it.isOnline }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        devices = devices,
                        totalDevices = devices.size,
                        onlineDevices = online
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load devices"
                    )
                }
            )
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onDone()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}