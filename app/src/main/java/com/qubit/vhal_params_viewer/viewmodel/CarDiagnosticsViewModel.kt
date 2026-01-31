package com.qubit.vhal_params_viewer.viewmodel

import androidx.lifecycle.ViewModel
import com.qubit.core_vhal.repository.HybridVhalReader
import com.qubit.core_vhal.repository.VhalReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CarDiagnosticsViewModel @Inject constructor(
    private val vhalReader: VhalReader
) : ViewModel() {

    init {
        connect()
    }

    val uiState = vhalReader.carState

    private val _isMockMode = MutableStateFlow(true)
    val isMockMode = _isMockMode.asStateFlow()

    fun connect() = vhalReader.connect()
    fun disconnect() = vhalReader.disconnect()

    fun toggleVhalSource(useMock: Boolean) {
        _isMockMode.value = useMock
        (vhalReader as? HybridVhalReader)?.toggleMode(useMock)
    }

    override fun onCleared() {
        disconnect()
        super.onCleared()
    }
}