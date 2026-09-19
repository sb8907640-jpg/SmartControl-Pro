package com.smartcontrol.owner.ui.consent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartcontrol.owner.data.model.Consent
import com.smartcontrol.owner.data.repository.ConsentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConsentUiState(
    val isLoading: Boolean = false,
    val consents: List<Consent> = emptyList(),
    val error: String? = null,
    val filter: String = "all" // all, granted, revoked
)

@HiltViewModel
class ConsentViewModel @Inject constructor(
    private val consentRepository: ConsentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConsentUiState())
    val uiState: StateFlow<ConsentUiState> = _uiState.asStateFlow()

    private var currentDeviceId: String = ""

    fun loadConsents(deviceId: String) {
        currentDeviceId = deviceId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = consentRepository.getConsents(deviceId)
            result.fold(
                onSuccess = { consents ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        consents = consents
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

    fun setFilter(filter: String) {
        _uiState.value = _uiState.value.copy(filter = filter)
    }

    fun grantConsent(featureName: String) {
        viewModelScope.launch {
            consentRepository.grantConsent(currentDeviceId, featureName)
            loadConsents(currentDeviceId)
        }
    }

    fun revokeConsent(featureName: String) {
        viewModelScope.launch {
            consentRepository.revokeConsent(currentDeviceId, featureName)
            loadConsents(currentDeviceId)
        }
    }

    fun revokeAll() {
        viewModelScope.launch {
            consentRepository.revokeAll(currentDeviceId)
            loadConsents(currentDeviceId)
        }
    }

    fun getFilteredConsents(): List<Consent> {
        return when (_uiState.value.filter) {
            "granted" -> _uiState.value.consents.filter { it.granted && it.revokedAt == null }
            "revoked" -> _uiState.value.consents.filter { !it.granted || it.revokedAt != null }
            else -> _uiState.value.consents
        }
    }
}