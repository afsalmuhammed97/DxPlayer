package com.afsal.dev.dxplayer.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.databinding.ActivityDashBordBinding
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.view_models.BaseViewModel

import com.google.android.material.bottomnavigation.BottomNavigationView

class DashBordActivity : AppCompatActivity() {

    private var readPermissionGrated=false
    private var writePermissionGranted=false
    private lateinit var permissionLauncher:ActivityResultLauncher<Array<String>>
    private lateinit var navController: NavController
    private lateinit var binding: ActivityDashBordBinding
    private val TAG="DashBordActivity"

   private val baseViewModel: BaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashBordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
      //  binding.navView.setItemIconTintList(null);


        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permission ->

            readPermissionGrated=permission[android.Manifest.permission.READ_EXTERNAL_STORAGE]?:readPermissionGrated
            writePermissionGranted=permission[android.Manifest.permission.WRITE_EXTERNAL_STORAGE]?:writePermissionGranted }



        updateOrRequestPermission()


      navController = findNavController(R.id.nav_host_fragment_activity_dash_bord)

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_video,
                R.id.navigation_files, R.id.navigation_images,R.id.navigation_music)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


          //  loadImages()



    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun loadImages(){

        baseViewModel.loadSystemImages(this)
        baseViewModel.photoList.observe(this,Observer<List<ImageModel>>{
                     Log.d(TAG,"data ${it.toString()}")
        })


    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_dash_bord)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun updateOrRequestPermission(){
        val hasReadPermission=ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_GRANTED

        val hasWritePermission=ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_GRANTED

        val minSdk=Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        readPermissionGrated=hasReadPermission
        writePermissionGranted=hasWritePermission ||minSdk

        val permissionRequest=  mutableListOf<String>()

        if (!writePermissionGranted){

            permissionRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!readPermissionGrated){
            permissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissionRequest.isNotEmpty()){

            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }

     private fun loadMediaData(){


     }

}