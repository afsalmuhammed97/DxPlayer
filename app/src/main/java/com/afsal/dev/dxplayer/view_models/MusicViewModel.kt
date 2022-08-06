package com.afsal.dev.dxplayer.view_models

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afsal.dev.dxplayer.models.audioSections.MusicItem
import com.afsal.dev.dxplayer.ui.services.MusicService
import com.afsal.dev.dxplayer.utills.CoreUttiles
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : BaseViewModel(application) {

    private val _musicList: MutableLiveData<List<MusicItem>> = MutableLiveData()
    val musicList: LiveData<List<MusicItem>>
        get() = _musicList

    init {

        loadAllMusicFiles()


            // val intent = Intent(context, MusicServices::class.java)
        //// need  foreground service call ***************************

       // requireActivity().bindService(intent,this, AppCompatActivity.BIND_AUTO_CREATE)
       // requireActivity().startService(intent)
    }

    fun loadAllMusicFiles() {

        viewModelScope.launch {
            _musicList.value = CoreUttiles.loadAllMusics(context)
        }

    }


}