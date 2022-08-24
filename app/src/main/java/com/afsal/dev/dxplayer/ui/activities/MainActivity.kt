package com.afsal.dev.dxplayer.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.afsal.dev.dxplayer.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

           Handler().postDelayed({
               val intent=Intent(this, DashBordActivity::class.java)
               startActivity(intent)
               this.finish()
           },1000)



    }
}