package com.afsal.dev.dxplayer.models.VideoSections

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Parcelize
data class VideoItemModel(
    val id:Long,
    val tittle:String,
    val size:String,
    val width:Int,
    val height:Int,
    val duration:Long,
    val dateAdded:String,
    val folderName:String,
    val artUri: Uri,
    val path:String
) :Parcelable    //Serializable
