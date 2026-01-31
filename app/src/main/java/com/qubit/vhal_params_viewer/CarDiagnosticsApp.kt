package com.qubit.vhal_params_viewer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CarDiagnosticsApp : Application()

const val VHAL_DEBUG = "VHAL_DEBUG"