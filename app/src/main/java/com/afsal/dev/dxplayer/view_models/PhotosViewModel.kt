package com.afsal.dev.dxplayer.view_models

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.*
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.utills.CoreUttiles
import kotlinx.coroutines.launch

class PhotosViewModel : BaseViewModel(){

       private var photosList= MutableLiveData<ImageModel>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }

         fun deletePhoto(photo:ImageModel,context: Context,intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>){

             viewModelScope.launch {
                 CoreUttiles.deletePhoto(photo.contentUri,context,intentSenderLauncher)
             }

         }






}