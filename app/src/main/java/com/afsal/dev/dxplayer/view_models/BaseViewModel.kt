package com.afsal.dev.dxplayer.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afsal.dev.dxplayer.models.ImageModel
import com.afsal.dev.dxplayer.utills.CorUttiles
import kotlinx.coroutines.launch

open class BaseViewModel :ViewModel() {



    private val _photosList:MutableLiveData<List<ImageModel>> = MutableLiveData()
     val photoList:LiveData<List<ImageModel>>
                    get() = _photosList

    fun loadSystemImages(context: Context){
        viewModelScope.launch {
          _photosList.value =  CorUttiles.loadPhotos(context)

        }
    }

}