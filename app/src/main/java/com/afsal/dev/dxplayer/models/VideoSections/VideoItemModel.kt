package com.afsal.dev.dxplayer.models.VideoSections

import android.net.Uri

data class VideoItemModel(
    val id:Long,
    val tittle:String,
    val size:String,
    val duration:Long,
    val dateAdded:String,
    val folderName:String,
    val artUri: Uri,

)
