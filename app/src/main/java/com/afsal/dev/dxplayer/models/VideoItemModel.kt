package com.afsal.dev.dxplayer.models

import android.net.Uri

data class VideoItemModel(
    val id:String,
    val tittle:String,
    val size:String,
    val duration:Long,
    val dateAdded:String,
    val folderName:String,
    val path:String,
    val artUri: Uri,

)
