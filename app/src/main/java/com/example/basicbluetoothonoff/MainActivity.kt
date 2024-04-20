package com.example.basicbluetoothonoff

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import com.example.basicbluetoothonoff.ui.theme.BasicBluetoothOnOffTheme

private const val REQUEST_BLUETOOTH_PERMISSION = 100

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasicBluetoothOnOffTheme {
                BluetoothControlScreen(this)
            }
        }
    }
}

@Composable
fun BluetoothControlScreen(mainActivity: MainActivity) {
    var isBluetoothEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bluetooth Control", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))
        Switch(
            checked = isBluetoothEnabled,
            onCheckedChange = { isChecked ->
                isBluetoothEnabled = isChecked
                // Call function to toggle Bluetooth
                toggleBluetooth(mainActivity, isChecked)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = if (isBluetoothEnabled) "Bluetooth Enabled" else "Bluetooth Disabled")
    }
}

private fun toggleBluetooth(context: Context, enable: Boolean) {
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    if (bluetoothAdapter == null) {
        // Device does not support Bluetooth
        // Handle this case
        return
    }

    if (enable) {
        val permissionsToRequest = arrayListOf<String>()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADMIN)
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(context as Activity, permissionsToRequest.toTypedArray(), REQUEST_BLUETOOTH_PERMISSION)
            return
        }

        bluetoothAdapter.enable()
    } else {
        bluetoothAdapter.disable()
    }
}
