package com.qubit.vhal_params_viewer

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.qubit.vhal_params_viewer.ui.screen.VhalParamsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity()  {
    val permissions = arrayOf("android.car.permission.CAR_ENERGY", "android.car.permission.CAR_TIRES", "android.car.permission.CAR_INFO")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPermissions(permissions)
        setContent {
            VhalParamsScreen()
        }
    }

    private fun getPermissions(permArray: Array<String>) {
        permArray.forEach { permission ->
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permArray, 101)
            } else {
                Log.d(VHAL_DEBUG, "$permission already granted")
            }
        }
    }
}

const val VHAL_DEBUG = "VHAL_DEBUG"