package com.afsal.dev.dxplayer.view_models

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.afsal.dev.dxplayer.dataBase.RecentVideoDatabase
import com.afsal.dev.dxplayer.models.VideoSections.Folders
import com.afsal.dev.dxplayer.models.VideoSections.PlayedVideoItem
import com.afsal.dev.dxplayer.models.VideoSections.VideoItemModel
import com.afsal.dev.dxplayer.repository.VideoRepository
import com.afsal.dev.dxplayer.ui.activities.DxPlayerActivity
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.utills.CoreUttiles.VIDEO
import com.afsal.dev.dxplayer.utills.CoreUttiles.VIDEO_POSITION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VidViewModel(application: Application) : BaseViewModel(application) {
    private val TAG="VidViewModel"
    private val videoRepository: VideoRepository

    private val _watchedHistory: MutableLiveData<List<PlayedVideoItem>> = MutableLiveData()
    val watchedHistory: LiveData<List<PlayedVideoItem>> get() = _watchedHistory

    init {
        val videoDao = RecentVideoDatabase.getVideoHistoryDatabase(application).videoHistoryDao()

        videoRepository = VideoRepository(videoDao)

        //loadVideosFromStorage()
        loadWatchHistory()


    }

    val foldersNameSet = mutableSetOf<String>()

    // val categoryVideoList= mutableListOf<Folders>()
    private val _categoryVideoList: MutableLiveData<List<Folders>> = MutableLiveData()
    val categoryVideoList: LiveData<List<Folders>>
        get() = _categoryVideoList


    private var _videoList: MutableLiveData<List<VideoItemModel>> = MutableLiveData()
    val videoList: LiveData<List<VideoItemModel>>
        get() = _videoList

    fun loadVideosFromStorage() {

        viewModelScope.launch(Dispatchers.IO) {
            val videos = CoreUttiles.loadVideos(context) { foldersNameSet.addAll(it) }
            _videoList.postValue(videos)
            Log.d(TAG, "Videos from local ${videos.toString()}")


            launch {

                _categoryVideoList.postValue(CoreUttiles.creatingCustomList(videos, foldersNameSet))
                Log.d(TAG, "categoryLsit  ${categoryVideoList.toString()}")
            }


        }

    }


    fun launchPlayerScreen(context: Context, video: VideoItemModel, fragment: Fragment): Intent {
        var videoPosition = 0L


        if (watchedHistory.value!!.isNotEmpty()) {

            for (item in watchedHistory.value!!) {
                if (item.videoId == video.id) {
                    videoPosition = item.lastPlayedPosition
                    break
                }
            }
        }


        val intent = Intent(context, DxPlayerActivity::class.java)
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(VIDEO, video as Parcelable)
        intent.putExtra(VIDEO_POSITION, videoPosition)
        fragment.startActivity(intent)
        return intent
    }


    private fun loadWatchHistory() {

        viewModelScope.launch {
            val videos = videoRepository.getLastWatchedVideos()

            Log.d("JJJ", "loaded History ${videos.toString()}")
            Log.d("JJJ", "loaded HistorySize ${videos.size}")
            _watchedHistory.postValue(videos)
        }
    }

    fun addVideoIntoHistory(video: PlayedVideoItem) {

        viewModelScope.launch(Dispatchers.IO) {


            viewModelScope.launch {
                videoRepository.addVideoToHistory(video)

            }

            loadWatchHistory()
        }
    }
}