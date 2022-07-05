package com.afsal.dev.dxplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.afsal.dev.dxplayer.ui.DashBordActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        val intent=Intent(this,DashBordActivity::class.java)
        startActivity(intent)
    }
}