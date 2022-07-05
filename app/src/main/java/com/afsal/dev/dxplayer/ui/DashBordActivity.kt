package com.afsal.dev.dxplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.databinding.ActivityDashBordBinding

import com.google.android.material.bottomnavigation.BottomNavigationView

class DashBordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashBordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashBordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
      //  binding.navView.setItemIconTintList(null);

        val navController = findNavController(R.id.nav_host_fragment_activity_dash_bord)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_video,
                R.id.navigation_files, R.id.navigation_images,R.id.navigation_music)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}