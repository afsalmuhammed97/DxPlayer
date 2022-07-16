package com.afsal.dev.dxplayer.utills

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.models.VideoItemModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

object CorUttiles {

    @SuppressLint("Range")
    fun loadVideos(context: Context):ArrayList<VideoItemModel>{

        val tempList=ArrayList<VideoItemModel>()
        val projection= arrayOf(
            MediaStore.Video.Media._ID,
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
  @RequiresApi(Build.VERSION_CODES.R)
  suspend     fun loadPhotos(context: Context):List<ImageModel>{
       return withContext(Dispatchers.IO){
      val collection= sdk29AndUp {
          MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
      }?:MediaStore.Images.Media.EXTERNAL_CONTENT_URI                        //EXTERNAL_CONTENT_URI

      val projection= arrayOf(
          MediaStore.Images.Media._ID,
          MediaStore.Images.Media.DISPLAY_NAME,
          MediaStore.Images.Media.SIZE,
          MediaStore.Images.Media.WIDTH,
          MediaStore.Images.Media.HEIGHT,
          MediaStore.Images.Media.DATE_TAKEN,
          MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
          MediaStore.Images.Media.ORIENTATION,
          MediaStore.Images.Media.TITLE,
      )
           val photos= mutableListOf<ImageModel>()

           val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

           context.contentResolver.query(
                 collection,
               projection,
               null,null,
                    imageSortOrder   //"${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} ASC"   // DESC
           )?.use { cursor ->
               val idColumn=cursor.getColumnIndex(MediaStore.Images.Media._ID)
               val displayNameColumn=cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
               val widthColumn=cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)
               val heightColumn=cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)
               val addedDateColumn=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN) //getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
               val folderNameColumn=cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
               val titleColumn=cursor.getColumnIndex(MediaStore.Images.Media.TITLE)
              // val titleColumn=cursor.getColumnIndex(MediaStore.Images.Media.OWNER_PACKAGE_NAME)

               while (cursor.moveToNext()){
                   val id= cursor.getLong(idColumn)
                   val displayName=cursor.getString(displayNameColumn)
                   val width=cursor.getInt(widthColumn)
                   val height=cursor.getInt(heightColumn)
                   val addedDate=cursor.getLong(addedDateColumn)
                   val folderName=cursor.getString(folderNameColumn)
                   val tittle=cursor.getString(titleColumn)
                   val contentUri=ContentUris.withAppendedId(
                       MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                   )


                   photos.add(
                       ImageModel(
                       id =id, name = displayName, width = width, height = height,
                                       contentUri = contentUri, addedDate = dateFormat(addedDate),          // dateFormater(addedDate) ,
                                       folderName =folderName, title = tittle )
                   )
               }
               photos.toList()

           }?: emptyList()
       }

    }
//    @SuppressLint("SimpleDateFormat")
//    @RequiresApi(Build.VERSION_CODES.N)
//     fun getDateFromUri(uri: Uri):String {
//        val split = uri.path!!.split("/").toTypedArray()
//        val fileName = split[split.size - 1]
//        val fileNameNoExt = fileName.split("\\.").toTypedArray()[0]
//        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        return format.format(Date(fileNameNoExt.toLong()))
//    }
//


//    @RequiresApi(Build.VERSION_CODES.N)
//    fun dateFormater(date:String):String{
//        val format = "MM-dd-yyyy HH:mm:ss"
//        val formatter = SimpleDateFormat(format, Locale.ENGLISH)
//
//        return formatter.format(Date(date.toLong()))
//
//    }

    fun dateFormat(date: Long):String{
        var myCal: Calendar = Calendar.getInstance()
        myCal.timeInMillis = date
        val dateText = Date(
            myCal[Calendar.YEAR] - 1900,
            myCal[Calendar.MONTH],
            myCal[Calendar.DAY_OF_MONTH],
            myCal[Calendar.HOUR_OF_DAY],
            myCal[Calendar.MINUTE]
        )
        val date=  android.text.format.DateFormat.format("MM/dd/yyyy", dateText).toString() //hh:mm
        return date
    }

    fun loadImageIntoView(imageUri:Uri,view:ImageView){

        Glide.with(view.context).load(imageUri)
            .apply(RequestOptions.placeholderOf(R.drawable.fish).centerCrop())
            .into(view)


    }
}