package com.afsal.dev.dxplayer.view_models

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.utills.CoreUttiles
import kotlinx.coroutines.launch

open class BaseViewModel(application: Application):AndroidViewModel(application) {

    val context = getApplication<Application>().applicationContext

    var imageList= arrayListOf<ImageModel>()
    var dateList= mutableSetOf<String>()

    private val _photosList:MutableLiveData<List<ImageModel>> = MutableLiveData()
     val photoList:LiveData<List<ImageModel>>
                    get() = _photosList

    @RequiresApi(Build.VERSION_CODES.R)
    fun loadSystemImages(){
        viewModelScope.launch {
          _photosList.value =  CoreUttiles.loadPhotos(context)


        }

    }

     fun getImageList():LiveData<List<ImageModel>>{
         return photoList
     }

}