package com.qubit.core_vhal.repository

import com.qubit.core_vhal.VhalParamsState
import kotlinx.coroutines.flow.StateFlow

interface VhalReader {
    val carState: StateFlow<VhalParamsState>

    val isConnected: StateFlow<Boolean>
    fun connect()
    fun disconnect()
}