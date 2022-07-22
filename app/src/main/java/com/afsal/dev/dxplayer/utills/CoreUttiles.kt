package com.afsal.dev.dxplayer.utills

import android.annotation.SuppressLint
import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.models.VideoSections.Folders
import com.afsal.dev.dxplayer.models.VideoSections.VideoItemModel
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*


object CoreUttiles {

    const val  IMAGE_FRAGMENT ="ImageFragment"
    const val IMAGE_VIEW_FRAGMENT="ImageViewFragment"
   const val VIDEO_FRAGMENT="VideoFragment"



    suspend fun loadVideos(context:Context, getFolderSet:((MutableSet<String>)->Unit) ):List<VideoItemModel>{
        val foldersNameSet= mutableSetOf<String>()

       return    withContext(Dispatchers.IO){
            val collection= sdk29AndUp {
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            }?:MediaStore.Video.Media.INTERNAL_CONTENT_URI    //EXTERNAL_CONTENT_URI

               val projection= arrayOf(
                   MediaStore.Video.Media._ID,
                   MediaStore.Video.Media.TITLE,
                   MediaStore.Video.Media.ALBUM,
                   MediaStore.Video.Media.DISPLAY_NAME,
                   MediaStore.Video.Media.DATE_ADDED,
                   MediaStore.Video.Media.DURATION,
                   MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                   MediaStore.Video.Media.BUCKET_ID,
                   MediaStore.Video.Media.SIZE,
                   MediaStore.Video.Media.DATA,
               )
               val videos = mutableListOf<VideoItemModel>()
               val sortOrder=MediaStore.Video.Media.DATE_TAKEN+" DESC"
               val   selection=MediaStore.Video.Media.DATA +" like?"
               val selectionArgs = arrayOf("%FolderName%")
               context.contentResolver.query(collection,projection,null,null,sortOrder)?.use { cursor->

                   val idColumn=cursor.getColumnIndex(MediaStore.Video.Media._ID)
                   val tittleColumn=cursor.getColumnIndex(MediaStore.Video.Media.TITLE)
                   val albumNameColumn=cursor.getColumnIndex(MediaStore.Video.Media.ALBUM)
                   val displayNameColumn=cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
                   val durationColumn=cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
                   val folderColumn=cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
                   val sizeColumn=cursor.getColumnIndex(MediaStore.Video.Media.SIZE)
                   val dateAddedColumn=cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)
                   val pathColumn=cursor.getColumnIndex(MediaStore.Video.Media.DATA)

                   while (cursor.moveToNext()){
                       val id=cursor.getLong(idColumn)
                       val tittle=cursor.getString(tittleColumn)
                       val albumName=cursor.getString(albumNameColumn)
                       val displayName=cursor.getString(displayNameColumn)
                       val size=cursor.getString(sizeColumn)
                       val duration=cursor.getLong(durationColumn)
                       val folderName=cursor.getString(folderColumn)
                       val dateAdded=cursor.getLong(dateAddedColumn)
                       val path=cursor.getString(pathColumn)
                     //  val dataUri=ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,id)

                            try {
                                val file=File(path)
                                val artUri=Uri.fromFile(file)

                                videos.add(
                                    VideoItemModel(id =id, tittle =  displayName, size =size , duration = duration,
                                    dateAdded = dateFormat(dateAdded), folderName = folderName,  artUri = artUri )
                                )
                                foldersNameSet.add(folderName)


                            }catch (e:Exception){}


                   }

                  // getFolderList(creatingCustomList(videos.toList(),foldersNameSet))
                    getFolderSet(foldersNameSet)
                   videos.toList()



               }?: emptyList()
        }

    }


     fun creatingCustomList(videoList:List<VideoItemModel>, folderSet:Set<String>):List<Folders>{
        val categoryVideoList= mutableListOf<Folders>()
        var folderList: List<VideoItemModel>

        for ( name in folderSet){

            folderList= videoList.filter {
                it.folderName == name
            }

            categoryVideoList.add(Folders(folderList,name))

        }
        return  categoryVideoList
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
          MediaStore.Images.Media.IS_FAVORITE,

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
               val favoriteColumn=cursor.getColumnIndex(MediaStore.Images.Media.IS_FAVORITE)

               while (cursor.moveToNext()){
                   val id= cursor.getLong(idColumn)
                   val displayName=cursor.getString(displayNameColumn)
                   val width=cursor.getInt(widthColumn)
                   val height=cursor.getInt(heightColumn)
                   val addedDate=cursor.getLong(addedDateColumn)
                   val folderName=cursor.getString(folderNameColumn)
                   val tittle=cursor.getString(titleColumn)
                   val isFavorite=cursor.getString(favoriteColumn)
                   val contentUri=ContentUris.withAppendedId(
                       MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                   )


                   photos.add(
                       ImageModel(
                       id =id, name = displayName, width = width, height = height,
                                       contentUri = contentUri, addedDate = dateFormat(addedDate),          // dateFormater(addedDate) ,
                                       folderName =folderName, title = tittle , isFavorite = isFavorite)
                   )
               }
               photos.toList()



           }?: emptyList()

       }


    }



    private fun dateFormat(date: Long): String {


        var myCal: Calendar = Calendar.getInstance()
        myCal.timeInMillis = date
        val dateText = Date(
            myCal[Calendar.YEAR] - 1900,
            myCal[Calendar.MONTH],
            myCal[Calendar.DAY_OF_MONTH],
            myCal[Calendar.HOUR_OF_DAY],
            myCal[Calendar.MINUTE],
            myCal[Calendar.AM_PM]

        )
        val date =
            android.text.format.DateFormat.format("dd/MM/yyyy hh:mm  a", dateText).toString() //hh:mm



        return date
    }



    fun loadImageIntoView(imageUri:Uri,view:ImageView?,customView: CustomZoomImageView? ,screenName:String){

        val requestOptions=RequestOptions()
               when(screenName){

                 IMAGE_FRAGMENT -> {
                       requestOptions.centerCrop()

                       if (view != null) {
                           Glide.with(view.context)
                               .load(imageUri)
                               .apply(requestOptions)
                               .placeholder(R.drawable.fish)
                               .into(view)
                       }

                   }

                   IMAGE_VIEW_FRAGMENT->{ requestOptions.centerInside()
                       if (customView != null) {
                           Glide.with(customView.context)
                               .load(imageUri)
                               .apply(requestOptions)
                               .into(customView)
                       }

               }
               }

    }

    fun shareImage(image:Uri,context: Context){


        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpg"
        shareIntent.putExtra(Intent.EXTRA_STREAM, image)
       context.startActivity(Intent.createChooser(shareIntent, "image"))
    }

    fun showSnackBar(text:String, layout: View){

              Snackbar.make(layout,text,Snackbar.LENGTH_SHORT)
                  .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                  .setBackgroundTint(Color.BLACK)
                  .setTextColor(Color.WHITE)
                 // .setAction("confirm.",){}
                  .show()

    }
//private lateinit var intentSenderLauncher:ActivityResultLauncher<IntentSenderRequest>
    suspend fun deletePhoto(photoUri:Uri,context: Context,intentSenderLauncher:ActivityResultLauncher<IntentSenderRequest>){
        withContext(Dispatchers.IO){

            try {
                context.contentResolver.delete(photoUri,null,null)
            }catch (e:SecurityException){
                 val intentSender= when{
                     Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ->{
                         MediaStore.createDeleteRequest(context.contentResolver, listOf(photoUri)).intentSender

                     }
                     Build.VERSION.SDK_INT >=Build.VERSION_CODES.Q ->{
                         val recoverableSecurityException= e as? RecoverableSecurityException
                         recoverableSecurityException?.userAction?.actionIntent?.intentSender
                     }
                     else -> null
                 }

                intentSender?.let { sender->
                    intentSenderLauncher.launch(
                        IntentSenderRequest.Builder(sender).build()
                    )
                }


            }
        }
    }



}