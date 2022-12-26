package com.example.androidhw

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity

class RequestPermissionsProvider {

    fun suggestUserToGrantPermissions(context: Context) {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )

        if (context.packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            AlertDialog.Builder(context)
                .setTitle("Permission denied")
                .setMessage(
                    "You have denied permission that is required for app work. " +
                            "You can change your decision in app settings. \n\n" +
                            "Would you like to open app settings?"
                )
                .setPositiveButton("Open app settings") { _, _ ->
                    startActivity(context, appSettingsIntent, null)
                }
                .setNeutralButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .setCancelable(false)
                .create()
                .show()
        }
    }

    fun suggestUserToTurnOnLocation(context: Context) {
        val locationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

        if (context.packageManager.resolveActivity(locationIntent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            AlertDialog.Builder(context)
                .setTitle("Location tracking is turned off")
                .setMessage(
                    "Your current location isn't tracking! " +
                            "You can provide location access in app settings. Otherwise, your last location will be used. \n\n" +
                            "Would you like to open app settings?"
                )
                .setPositiveButton("Open app settings") { _, _ ->
                    startActivity(context, locationIntent, null)
                }
                .setNeutralButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .setNegativeButton("Never ask again") { dialog, _ ->
                    context.getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)?.edit()
                        ?.putBoolean(MainActivity.SHOULD_REQUEST_LOCATION, false)
                        ?.apply()

                    dialog.cancel()
                }
                .setCancelable(false)
                .create()
                .show()
        }
    }
}
