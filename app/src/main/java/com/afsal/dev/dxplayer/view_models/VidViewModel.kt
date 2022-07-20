package com.afsal.dev.dxplayer.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afsal.dev.dxplayer.models.VideoItemModel
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.utills.CoreUttiles
import kotlinx.coroutines.launch

class VidViewModel : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text



    private var _videoList:MutableLiveData<List<VideoItemModel>> = MutableLiveData()
    val videoList:LiveData<List<VideoItemModel>>
        get() = _videoList

    fun loadVideosFromStorage(context: Context){

        viewModelScope.launch {
          _videoList.value =  CoreUttiles.loadVideos(context)
        }
    }

}