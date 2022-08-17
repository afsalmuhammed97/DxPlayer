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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afsal.dev.dxplayer.dataBase.MusicDao
import com.afsal.dev.dxplayer.dataBase.SongsDatabase
import com.afsal.dev.dxplayer.models.audioSections.MusicItem
import com.afsal.dev.dxplayer.models.audioSections.PlayLists
import com.afsal.dev.dxplayer.repository.MusicRepository
import com.afsal.dev.dxplayer.ui.services.MusicService
import com.afsal.dev.dxplayer.utills.CoreUttiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : BaseViewModel(application) {

     private val TAG="MusicViewModel"
     private lateinit var repository: MusicRepository
  //  var allPlayList:LiveData<List<PlayLists>>
    val isPlaying=MutableLiveData<Boolean>()
    private val _musicList: MutableLiveData<List<MusicItem>> = MutableLiveData()
    val musicList: LiveData<List<MusicItem>>
        get() = _musicList




    init {

    // val musicDao=SongsDatabase.getDatabase(application).musicDao()

     // repository=MusicRepository(musicDao)

//          allPlayList=repository.allPlayList
//       Log.d(TAG,"allPlaylist ${allPlayList.value.toString()}")
//       Log.d(TAG,"allPlaylist from repo ${repository.allPlayList.value.toString()}")
    }



    fun createPlayList(name:String){
        val playList=PlayLists(name)

        viewModelScope.launch {
            repository.createPlayList(playList)
        }
        //allPlayList=repository.allPlayList
    }

    fun loadAllMusicFiles() {

        viewModelScope.launch (Dispatchers.IO) {
            _musicList.postValue(CoreUttiles.loadAllMusics(context))           //CoreUttiles.loadAllMusics(context)
        }

    }





}