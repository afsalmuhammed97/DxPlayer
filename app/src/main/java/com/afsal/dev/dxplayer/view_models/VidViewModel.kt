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
import com.afsal.dev.dxplayer.models.VideoSections.Folders
import com.afsal.dev.dxplayer.models.VideoSections.PlayedVideoItem
import com.afsal.dev.dxplayer.models.VideoSections.VideoItemModel
import com.afsal.dev.dxplayer.ui.activities.DxPlayerActivity
import com.afsal.dev.dxplayer.ui.activities.PlayerScreenActivity
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.utills.CoreUttiles.VIDEO
import com.afsal.dev.dxplayer.utills.CoreUttiles.VIDEO_POSITION
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VidViewModel(application: Application) : BaseViewModel(application) {

    val recentVideosList= mutableListOf<PlayedVideoItem>()
     val recentVideoLiveData=MutableLiveData<List<PlayedVideoItem>>()



//   val scope= CoroutineScope(Dispatchers.IO + CoroutineName("myScope"))
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }

    val foldersNameSet= mutableSetOf<String>()
// val categoryVideoList= mutableListOf<Folders>()
private val _categoryVideoList:MutableLiveData<List<Folders>> = MutableLiveData()
    val categoryVideoList:LiveData<List<Folders>>
    get() = _categoryVideoList


    private var _videoList:MutableLiveData<List<VideoItemModel>> = MutableLiveData()
    val videoList:LiveData<List<VideoItemModel>>
        get() = _videoList

    fun loadVideosFromStorage(){

        viewModelScope.launch {
       val videos =  CoreUttiles.loadVideos(context){              foldersNameSet.addAll(it)  }
            _videoList.value=videos



           // scope.launch {}
             _categoryVideoList.value=  CoreUttiles.creatingCustomList(videos,foldersNameSet)


        }

    }


    fun launchPlayerScreen(context: Context,video:VideoItemModel,fragment:Fragment): Intent {
         var videoPostion=0L
        if (recentVideosList.isNotEmpty())
        for (item in recentVideosList){
            if (item.videoId==video.id){
                videoPostion=item.lastPlayedPosition
                break
            }
        }

        val intent = Intent(context,DxPlayerActivity::class.java)
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(VIDEO,video as Parcelable)
        intent.putExtra(VIDEO_POSITION,videoPostion)
        fragment.startActivity(intent)
        return intent
    }



}