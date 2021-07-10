package com.example.testapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

// Create a broadcast receiver class based on class BroadcastReceiver
// to receive dynamic Android OS system broadcast intents
class AirplaneBroadCastReceiver : BroadcastReceiver() {

    // Override the onReceive() method to get intent extra data
    override fun onReceive(context: Context?, intent: Intent?) {
        val isEnabled = intent?.getBooleanExtra("state", false) ?: return // ?: checks for null. If null just return

        if (isEnabled) {
            Toast.makeText(context, "Airplane mode enabled", Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(context, "Airplane mode disabled", Toast.LENGTH_SHORT).show()
        }
    }
}