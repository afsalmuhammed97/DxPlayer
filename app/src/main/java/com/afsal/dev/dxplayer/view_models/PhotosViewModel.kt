package com.afsal.dev.dxplayer.view_models

import androidx.lifecycle.*
import com.afsal.dev.dxplayer.models.photosSections.ImageModel

class PhotosViewModel : BaseViewModel(){

       private var photosList= MutableLiveData<ImageModel>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }








}