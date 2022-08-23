package com.afsal.dev.dxplayer.view_models

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.afsal.dev.dxplayer.dataBase.SongsDatabase
import com.afsal.dev.dxplayer.models.audioSections.MusicItem
import com.afsal.dev.dxplayer.models.audioSections.PlayListWithSongs
import com.afsal.dev.dxplayer.models.audioSections.PlayLists
import com.afsal.dev.dxplayer.repository.MusicRepository
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.utills.CoreUttiles.FAVOURITE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : BaseViewModel(application) {

    private val TAG = "MusicViewModel"
     var currentPlayListName:String =""
    private var repository: MusicRepository
    var playListNames: LiveData<List<PlayLists>>
    val isPlaying = MutableLiveData<Boolean>()
    private val _songsInPlayList: MutableLiveData<List<MusicItem>> = MutableLiveData()
    val songsInPlayList: LiveData<List<MusicItem>>
        get() = _songsInPlayList

    private val _musicList: MutableLiveData<List<MusicItem>> = MutableLiveData()
    val musicList: LiveData<List<MusicItem>>
        get() = _musicList

   private val _isFavourite:MutableLiveData<Boolean> = MutableLiveData()
    val isFavourite:LiveData<Boolean>
    get() = _isFavourite
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


            //add song to playlist
            repository.addSongToPlayList(songWithPlayListName)

            //getting the playlistSongCount
            val playListsData=repository.getPlayListData(playListName)

            if (playListsData.playListImage ==null){


                val playLists=PlayLists(playListName,playListsData.songsCount+1,song.imageUri)
                repository.updatePlayListWithSongCount(playLists)


            }else{
                //updating songCount

                val playLists=PlayLists(playListName,playListsData.songsCount+1)
                repository.updatePlayListWithSongCount(playLists)
            }



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

    fun checkTheCurrentSongIsFavOrNot(currentSong:MusicItem){

       //
        viewModelScope.launch (Dispatchers.IO){
            val isFav=repository.checkSongExistence(currentSong.id,FAVOURITE)

            _isFavourite.postValue(isFav)
        }


    }

    fun deleteSongFromPlaylist(song:MusicItem,playlist: String){

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSongFromPlayList(song.id,playlist)
        }

        getSongsInPlayList(playlist)
    }

    fun deletePlayList(playlist: String){

        viewModelScope.launch(Dispatchers.IO) {

            repository.deletePlayList(playlist)
        }
    }

}