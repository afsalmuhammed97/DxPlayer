package com.afsal.dev.dxplayer.view_models

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afsal.dev.dxplayer.models.audioSections.MusicItem
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var exoPlayer: SimpleExoPlayer
   private val _musicList:MutableLiveData<List<MusicItem>> = MutableLiveData()
    val musicList:LiveData<List<MusicItem>>
    get() = _musicList

    init {

        loadAllMusicFiles()
    }

    fun loadAllMusicFiles(){

        viewModelScope.launch {
            _musicList.value=CoreUttiles.loadAllMusics(context)
        }

    }

    fun initPlayer(){


    }

}