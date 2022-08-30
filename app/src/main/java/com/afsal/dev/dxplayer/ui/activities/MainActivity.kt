package com.afsal.dev.dxplayer.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.afsal.dev.dxplayer.R

class MainActivity : AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var readPermissionGrated = false
    private var writePermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()


        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->

                readPermissionGrated = permission[android.Manifest.permission.READ_EXTERNAL_STORAGE]
                    ?: readPermissionGrated
                writePermissionGranted =
                    permission[android.Manifest.permission.WRITE_EXTERNAL_STORAGE]
                        ?: writePermissionGranted

                Log.d(
                    "MM",
                    "readPermission $readPermissionGrated and writepermission $writePermissionGranted "
                )

                moveToDashBoardActivity()
            }




        updateOrRequestPermission()

        moveToDashBoardActivity()


    }

    private fun moveToDashBoardActivity() {
        if (readPermissionGrated && writePermissionGranted) {

            Handler().postDelayed({
                val intent = Intent(this, DashBordActivity::class.java)
                startActivity(intent)
                this.finish()
            }, 500)

        }

    }

    private fun updateOrRequestPermission() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val minSdk = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        readPermissionGrated = hasReadPermission
        writePermissionGranted = hasWritePermission || minSdk


        val permissionRequest = mutableListOf<String>()

        if (!writePermissionGranted) {

            permissionRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!readPermissionGrated) {
            permissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }


        if (permissionRequest.isNotEmpty()) {

            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }
}