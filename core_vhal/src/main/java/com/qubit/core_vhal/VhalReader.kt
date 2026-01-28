package com.qubit.core_vhal

import kotlinx.coroutines.flow.StateFlow

interface VhalReader {
    val carState: StateFlow<VhalParamsState>
    fun connect()
    fun disconnect()
}