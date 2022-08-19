package com.afsal.dev.dxplayer.view_models

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.afsal.dev.dxplayer.dataBase.SongsDatabase
import com.afsal.dev.dxplayer.models.audioSections.MusicItem
import com.afsal.dev.dxplayer.models.audioSections.PlayListWithSongs
import com.afsal.dev.dxplayer.models.audioSections.PlayLists
import com.afsal.dev.dxplayer.repository.MusicRepository
import com.afsal.dev.dxplayer.utills.CoreUttiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : BaseViewModel(application) {

    private val TAG = "MusicViewModel"

    private var repository: MusicRepository
    var playListNames: LiveData<List<PlayLists>>
    val isPlaying = MutableLiveData<Boolean>()
    private val _songsInPlayList: MutableLiveData<List<MusicItem>> = MutableLiveData()
    val songsInPlayList: LiveData<List<MusicItem>>
        get() = _songsInPlayList

    private val _musicList: MutableLiveData<List<MusicItem>> = MutableLiveData()
    val musicList: LiveData<List<MusicItem>>
        get() = _musicList


    init {

        val musicDao = SongsDatabase.getDatabase(application).musicDao()

        repository = MusicRepository(musicDao)


        playListNames = repository.allPlayListNames

    }


    fun createPlayList(name: String) {
        val playList = PlayLists(name)

        viewModelScope.launch {
            repository.createPlayList(playList)
        }
        playListNames = repository.allPlayListNames
        //  repository.getPlaylist()
    }

    fun loadAllMusicFiles() {

        viewModelScope.launch(Dispatchers.IO) {
            _musicList.postValue(CoreUttiles.loadAllMusics(context))           //CoreUttiles.loadAllMusics(context)
        }

    }


    fun addSongsToPlaylist(song: MusicItem, playListName: String) {
        val songWithPlayListName = PlayListWithSongs(
            song.id, song.tittle,
            song.album, song.artist, song.duration,
            song.imageUri, song.artUri, song.folderName, playListName
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.addSongToPlayList(songWithPlayListName)
        }

    }

    fun getSongsInPlayList(playlist: String) {

        viewModelScope.launch(Dispatchers.IO) {

            val songs = repository.getAllSongsInPlayList(playlist)

            val songList = mutableListOf<MusicItem>()                 //<MusicItem>()
            songs.map {

                val song = MusicItem(
                    id = it.id, tittle = it.tittle, album = it.album, artist = it.artist,
                    duration = it.duration, imageUri = it.imageUri, it.artUri, it.folderName
                )

                songList.add(song)
            }
            _songsInPlayList.postValue(songList)

        }
    }

}