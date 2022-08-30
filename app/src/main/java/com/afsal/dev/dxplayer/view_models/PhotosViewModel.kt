package com.afsal.dev.dxplayer.view_models

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.utills.CoreUttiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotosViewModel(application: Application) : BaseViewModel(application){

       private var photosList= MutableLiveData<ImageModel>()


    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }




    private val _photosList:MutableLiveData<List<ImageModel>> = MutableLiveData()
    val photoList:LiveData<List<ImageModel>>
        get() = _photosList

    @RequiresApi(Build.VERSION_CODES.R)
    fun loadSystemImages(){
        viewModelScope.launch (Dispatchers.IO){
            _photosList.postValue( CoreUttiles.loadPhotos(context))


        }

    }



         fun deletePhoto(photo:ImageModel,context: Context,intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>){

             viewModelScope.launch {
                 CoreUttiles.deletePhoto(photo.contentUri,context,intentSenderLauncher)
             }

         }






}