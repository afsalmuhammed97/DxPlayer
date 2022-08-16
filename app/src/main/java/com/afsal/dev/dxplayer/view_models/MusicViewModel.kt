package com.afsal.dev.dxplayer.view_models

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afsal.dev.dxplayer.models.audioSections.MusicItem
import com.afsal.dev.dxplayer.ui.services.MusicService
import com.afsal.dev.dxplayer.utills.CoreUttiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : BaseViewModel(application) {

     private val TAG="MusicViewModel"

    val isPlaying=MutableLiveData<Boolean>()
    private val _musicList: MutableLiveData<List<MusicItem>> = MutableLiveData()
    val musicList: LiveData<List<MusicItem>>
        get() = _musicList




    init {




    }

    fun loadAllMusicFiles() {

        viewModelScope.launch (Dispatchers.IO) {
            _musicList.postValue(CoreUttiles.loadAllMusics(context))           //CoreUttiles.loadAllMusics(context)
        }

    }





}