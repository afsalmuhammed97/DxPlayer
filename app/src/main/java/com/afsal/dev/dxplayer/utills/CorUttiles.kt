package com.afsal.dev.dxplayer.utills

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.afsal.dev.dxplayer.models.ImageModel
import com.afsal.dev.dxplayer.models.VideoItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object CorUttiles {

    @SuppressLint("Range")
    fun loadVideos(context: Context):ArrayList<VideoItemModel>{

        val tempList=ArrayList<VideoItemModel>()
        val projection= arrayOf( MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.ALBUM,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
        )
        val cursor=context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection,null,null,MediaStore.Video.Media.DATE_ADDED+" DESC")

        if (cursor!=null){
            if (cursor.moveToNext()){
                do {

                    val idC=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val tittleC=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val albumC=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM))
                    val folderC=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val dateAddedC=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
                    val durationC=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)).toLong()
                    val sizeC=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val pathC=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))

                    try {
                        val file=File(pathC)
                        val artUriC= Uri.fromFile(file)
                        val videoData=VideoItemModel(id = idC, tittle = tittleC, size = sizeC,
                                                     duration = durationC, folderName = folderC,
                                                     dateAdded=dateAddedC, path = pathC, artUri = artUriC)

                         if (file.exists()){
                             tempList.add(videoData)
                         }
                    }catch (e:Exception){
                          e.printStackTrace()
                    }



                }while (cursor.moveToNext())

                cursor.close()
            }
        }

     return  tempList
    }

    //to load images from storage
  suspend     fun loadPhotos(context: Context):List<ImageModel>{
       return withContext(Dispatchers.IO){
      val collection= sdk29AndUp {
          MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
      }?:MediaStore.Images.Media.INTERNAL_CONTENT_URI                       //EXTERNAL_CONTENT_URI

      val projection= arrayOf(
          MediaStore.Images.Media._ID,
          MediaStore.Images.Media.DISPLAY_NAME,
          MediaStore.Images.Media.WIDTH,
          MediaStore.Images.Media.HEIGHT,
          MediaStore.Images.Media.DATE_ADDED,
      )
           val photos= mutableListOf<ImageModel>()
           context.contentResolver.query(
               collection,
               projection,
               null,null,
               "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
           )?.use { cursor ->
               val idColumn=cursor.getColumnIndex(MediaStore.Images.Media._ID)
               val displayNameColumn=cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
               val widthColumn=cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)
               val heightColumn=cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)
               val addedDateColumn=cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)

               while (cursor.moveToNext()){
                   val id= cursor.getLong(idColumn)
                   val displayName=cursor.getString(displayNameColumn)
                   val width=cursor.getInt(widthColumn)
                   val height=cursor.getInt(heightColumn)
                   val addedDate=cursor.getString(addedDateColumn)
                   val contentUri=ContentUris.withAppendedId(
                       MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                   )
                   photos.add(ImageModel(id=id, name = displayName, width = width, height = height, contentUri = contentUri, addedDate = addedDate))
               }
               photos.toList()

           }?: emptyList()
       }

    }

    fun loadImage(){
        
    }
}