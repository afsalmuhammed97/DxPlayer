package com.afsal.dev.dxplayer.view_models

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.afsal.dev.dxplayer.models.VideoSections.Folders
import com.afsal.dev.dxplayer.models.VideoSections.VideoItemModel
import com.afsal.dev.dxplayer.utills.CoreUttiles
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VidViewModel(application: Application) : BaseViewModel(application) {
  //  val scope= CoroutineScope(Dispatchers.IO + CoroutineName("myScope"))
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

  val categoryVideoList= mutableListOf<Folders>()
  val foldersNameSet= mutableSetOf<String>()


    private var _videoList:MutableLiveData<List<VideoItemModel>> = MutableLiveData()
    val videoList:LiveData<List<VideoItemModel>>
        get() = _videoList

    fun loadVideosFromStorage(){

        viewModelScope.launch {
          _videoList.value =  CoreUttiles.loadVideos(context){

                             // foldersNameSet.addAll(it)
                                categoryVideoList.addAll(it)
          }


        }





    }


}