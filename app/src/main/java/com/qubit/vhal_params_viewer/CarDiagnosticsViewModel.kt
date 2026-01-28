package com.qubit.vhal_params_viewer

import androidx.lifecycle.ViewModel
import com.qubit.core_vhal.VhalReader
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CarDiagnosticsViewModel @Inject constructor(
    private val vhalReader: VhalReader
) : ViewModel() {
    val state = vhalReader.carState

    init {
        vhalReader.connect()
    }

    override fun onCleared() {
        vhalReader.disconnect()
        super.onCleared()
    }
}
