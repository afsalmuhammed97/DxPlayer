package com.afsal.dev.dxplayer.models.photosSections

import android.net.Uri
import android.util.Log
import java.io.Serializable

data class ImageModel(
    val id:Long,
    val name:String,
    val width:Int,
    val height:Int,
    val contentUri:Uri,
    val addedDate:String?,
    val folderName:String,
    val title: String?,
    val isFavorite:String
):Serializable
