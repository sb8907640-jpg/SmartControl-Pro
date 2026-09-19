package com.smartcontrol.owner.ui.devicedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartcontrol.owner.data.model.Consent
import com.smartcontrol.owner.data.model.Device
import com.smartcontrol.owner.data.repository.ConsentRepository
import com.smartcontrol.owner.data.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DeviceDetailUiState(
    val isLoading: Boolean = false,
    val device: Device? = null,
    val consents: List<Consent> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val consentRepository: ConsentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeviceDetailUiState())
    val uiState: StateFlow<DeviceDetailUiState> = _uiState.asStateFlow()

    fun loadDevice(deviceId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val deviceResult = deviceRepository.getDevice(deviceId)
            val consentResult = consentRepository.getConsents(deviceId)

            deviceResult.fold(
                onSuccess = { device ->
                    _uiState.value = _uiState.value.copy(device = device)
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
            )

            consentResult.fold(
                onSuccess = { consents ->
                    _uiState.value = _uiState.value.copy(consents = consents)
                },
                onFailure = { /* ignore */ }
            )

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun unlinkDevice(deviceId: String, reason: String?, onDone: () -> Unit) {
        viewModelScope.launch {
            val result = deviceRepository.unlinkDevice(deviceId, reason)
            result.fold(
                onSuccess = { onDone() },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
            )
        }
    }

    fun revokeAll(deviceId: String) {
        viewModelScope.launch {
            consentRepository.revokeAll(deviceId)
            loadDevice(deviceId)
        }
    }
}