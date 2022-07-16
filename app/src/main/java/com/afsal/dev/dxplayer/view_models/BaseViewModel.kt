package com.afsal.dev.dxplayer.view_models

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.utills.CorUttiles
import kotlinx.coroutines.launch

open class BaseViewModel :ViewModel() {

    private var images= listOf<ImageModel>()
    var dateList= mutableSetOf<String>()

    private val _photosList:MutableLiveData<List<ImageModel>> = MutableLiveData()
     val photoList:LiveData<List<ImageModel>>
                    get() = _photosList

    @RequiresApi(Build.VERSION_CODES.R)
    fun loadSystemImages(context: Context){
        viewModelScope.launch {
          _photosList.value =  CorUttiles.loadPhotos(context)


        }



    }



}