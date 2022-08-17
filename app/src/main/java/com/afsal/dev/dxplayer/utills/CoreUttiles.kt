package com.afsal.dev.dxplayer.utills

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.models.VideoSections.Folders
import com.afsal.dev.dxplayer.models.VideoSections.PlayedVideoItem
import com.afsal.dev.dxplayer.models.VideoSections.VideoItemModel
import com.afsal.dev.dxplayer.models.audioSections.MusicItem
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jsibbold.zoomage.ZoomageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*


object CoreUttiles {

    const val IMAGE_VIEW = "ImageFragment"
    const val CUSTOM_IMAGE_VIEW = "CustomImageView"
    const val VIDEO_FRAGMENT = "VideoFragment"
    const val VIDEO = "VideoItem"
    const val LASTPLAYED_VIDEO = "LastPlayedVideo"
    const val LASTPLAYED_DATA = "LastPlayedData"
    const val VIDEO_POSITION = "VideoPosition"
    const val VIDEO_ID = "VideoId"
    const val VIDEO_URI = "VideoUri"
    const val VIDEO_DURATION = "VideoDuration"
    const val SONGS_TABLE="SongsTable"
    const val PLAYLIST_SONGS_TABLE="PlayListSongs"
    const val PLAYLIST_TABLE="PlayListTable"
    const val MUSIC_DATA_BASE="MusicDatabase"

    suspend fun loadVideos(
        context: Context,
        getFolderSet: ((MutableSet<String>) -> Unit)
    ): List<VideoItemModel> {
        val foldersNameSet = mutableSetOf<String>()

        return withContext(Dispatchers.IO) {
            val collection = sdk29AndUp {
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } ?: MediaStore.Video.Media.INTERNAL_CONTENT_URI    //EXTERNAL_CONTENT_URI

            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.ALBUM,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.DATA,
            )
            val videos = mutableListOf<VideoItemModel>()
            val sortOrder = MediaStore.Video.Media.DATE_TAKEN + " DESC"
            val selection = MediaStore.Video.Media.DATA + " like?"
            val selectionArgs = arrayOf("%FolderName%")
            context.contentResolver.query(collection, projection, null, null, sortOrder)
                ?.use { cursor ->

                    val idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID)
                    val tittleColumn = cursor.getColumnIndex(MediaStore.Video.Media.TITLE)
                    val albumNameColumn = cursor.getColumnIndex(MediaStore.Video.Media.ALBUM)
                    val displayNameColumn =
                        cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
                    val durationColumn = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
                    val folderColumn =
                        cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
                    val sizeColumn = cursor.getColumnIndex(MediaStore.Video.Media.SIZE)
                    val dateAddedColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)
                    val pathColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
                    val heightColumn = cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT)
                    val widthColumn = cursor.getColumnIndex(MediaStore.Video.Media.WIDTH)


                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val tittle = cursor.getString(tittleColumn)
                        val albumName = cursor.getString(albumNameColumn)
                        val displayName = cursor.getString(displayNameColumn)
                        val size = cursor.getString(sizeColumn)
                        val duration = cursor.getLong(durationColumn)
                        val folderName = cursor.getString(folderColumn)
                        val dateAdded = cursor.getLong(dateAddedColumn)
                        val path = cursor.getString(pathColumn)
                        val height = cursor.getInt(heightColumn)
                        val width = cursor.getInt(widthColumn)

                        //  val dataUri=ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,id)


                        try {
                            val file = File(path)
                            val artUri = Uri.fromFile(file)

                            videos.add(
                                VideoItemModel(
                                    id = id,
                                    tittle = displayName,
                                    size = size,
                                    duration = duration,
                                    dateAdded = dateFormat(dateAdded),
                                    folderName = folderName,
                                    artUri = artUri,
                                    width = width,
                                    height = height,
                                    path = path
                                )
                            )
                            foldersNameSet.add(folderName)


                        } catch (e: Exception) {
                        }


                    }

                    // getFolderList(creatingCustomList(videos.toList(),foldersNameSet))
                    getFolderSet(foldersNameSet)
                    videos.toList()


                } ?: emptyList()
        }

    }


    fun creatingCustomList(videoList: List<VideoItemModel>, folderSet: Set<String>): List<Folders> {
        val categoryVideoList = mutableListOf<Folders>()
        var folderList: List<VideoItemModel>

        for (name in folderSet) {

            folderList = videoList.filter {
                it.folderName == name
            }

            categoryVideoList.add(Folders(folderList, name))

        }
        return categoryVideoList
    }

    //to load all musics from storage
    suspend fun loadAllMusics(context: Context): List<MusicItem> {

        //  val sortOrder= arrayOf( "${MediaStore.Audio.Media.DATE_TAKEN} DESC")

        return withContext(Dispatchers.IO) {

            val musicList = mutableListOf<MusicItem>()
            val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
            val projection = arrayOf(
                MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.BUCKET_DISPLAY_NAME
            )

            val collection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            context.contentResolver.query(

                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                selection, null,
                MediaStore.Audio.Media.DURATION + ">= 60000", null

//                collection, projection, selection, null,
//                MediaStore.Audio.Media.DURATION + ">= 60000", null
            )?.use { cursor ->

                val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val tittleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
                val artistsColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                val albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                val artUriColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
                val folderNameColumn =
                    cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val tittle = cursor.getString(tittleColumn)
                    val album = cursor.getString(albumColumn)
                    val artists = cursor.getString(artistsColumn)
                    val duration = cursor.getLong(durationColumn)
//                   val path=cursor.getString(pathColumn)
                    val uri = cursor.getString(artUriColumn)
                    val folder = cursor.getString(folderNameColumn)
                    val albumId = cursor.getString(albumIdColumn)


                    try {
                        val uri1 = Uri.parse("content://media/external/audio/albumart")
                        val imageUri = Uri.withAppendedPath(uri1, albumId).toString()
                         val file=File(uri)
                          val artUri=Uri.fromFile(file).toString()

                        musicList.add(
                            MusicItem(
                                id = id, tittle = tittle, album = album,
                                artist = artists, duration = duration,
                                imageUri = imageUri, artUri = artUri, folderName = folder
                            )
                        )

                    } catch (e: Exception) {
                        e.stackTrace
                    }

                }

                musicList.toList()

            } ?: emptyList()

        }

    }

    //to load images from storage
    @RequiresApi(Build.VERSION_CODES.R)
    suspend fun loadPhotos(context: Context): List<ImageModel> {
        return withContext(Dispatchers.IO) {
            val collection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.TITLE,
//          MediaStore.Images.Media.IS_FAVORITE,

            )
            val photos = mutableListOf<ImageModel>()

            val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

            context.contentResolver.query(
                collection,
                projection,
                null, null,
                imageSortOrder   //"${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} ASC"   // DESC
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val displayNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)
                val heightColumn = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)
                val addedDateColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN) //getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                val folderNameColumn =
                    cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val titleColumn = cursor.getColumnIndex(MediaStore.Images.Media.TITLE)
//               val favoriteColumn=cursor.getColumnIndex(MediaStore.Images.Media.IS_FAVORITE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)
                    val addedDate = cursor.getLong(addedDateColumn)
                    val folderName = cursor.getString(folderNameColumn)
                    val tittle = cursor.getString(titleColumn)
//                   val isFavorite=cursor.getString(favoriteColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                    )


                    photos.add(
                        ImageModel(
                            id = id,
                            name = displayName,
                            width = width,
                            height = height,
                            contentUri = contentUri,
                            addedDate = dateFormat(addedDate),          // dateFormater(addedDate) ,
                            folderName = folderName,
                            title = tittle
                        )
                    )
                }

                //  cursor.close()
                photos.toList()


            } ?: emptyList()

        }


    }

    fun durationToHour(duration: Long): String = DateUtils.formatElapsedTime(duration / 1000)

    fun generateThumbNail(path: String): Bitmap {

        val videoThumb =
            ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND)

        return videoThumb!!
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
            android.text.format.DateFormat.format("dd/MM/yyyy hh:mm  a", dateText)
                .toString() //hh:mm



        return date
    }


    fun loadImageIntoView(
        imageUri: Uri,
        view: ImageView?,
        customView: ZoomageView?,
        screenType: String
    ) {

        val requestOptions = RequestOptions()
        when (screenType) {

            IMAGE_VIEW -> {
                requestOptions.centerCrop()

                if (view != null) {
                    Glide.with(view.context)
                        .load(imageUri)
                        .apply(requestOptions)
                        .placeholder(R.drawable.fish)
                        .into(view)
                }

            }

            CUSTOM_IMAGE_VIEW -> {
                requestOptions.centerInside()
                if (customView != null) {
                    Glide.with(customView.context)
                        .load(imageUri)
                        .apply(requestOptions)
                        .into(customView)
                }

            }
        }

    }

    fun shareImage(image: Uri, context: Context) {


        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpg"
        shareIntent.putExtra(Intent.EXTRA_STREAM, image)
        context.startActivity(Intent.createChooser(shareIntent, "image"))
    }

    fun showSnackBar(text: String, layout: View, actionText: String?, actionClicked: () -> Unit) {

        Snackbar.make(layout, text, Snackbar.LENGTH_SHORT)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setBackgroundTint(Color.BLACK)
            .setTextColor(Color.WHITE)
            .setAction(actionText) {
                actionClicked()
            }
            .show()

    }

    //private lateinit var intentSenderLauncher:ActivityResultLauncher<IntentSenderRequest>
    suspend fun deletePhoto(
        photoUri: Uri,
        context: Context,
        intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        withContext(Dispatchers.IO) {

            try {
                context.contentResolver.delete(photoUri, null, null)
            } catch (e: SecurityException) {
                val intentSender = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                        MediaStore.createDeleteRequest(
                            context.contentResolver,
                            listOf(photoUri)
                        ).intentSender

                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                        val recoverableSecurityException = e as? RecoverableSecurityException
                        recoverableSecurityException?.userAction?.actionIntent?.intentSender
                    }
                    else -> null
                }

                intentSender?.let { sender ->
                    intentSenderLauncher.launch(
                        IntentSenderRequest.Builder(sender).build()
                    )
                }


            }
        }
    }


    fun saveLastPlayedVideo(context: Context, currentPosition: Long, video: VideoItemModel) {

        // val lastPlayed= GsonBuilder().create().toJson(lastPlayedVideo)

        val sharedPreference = context.getSharedPreferences(LASTPLAYED_VIDEO, Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()

        editor.apply {
            putLong(VIDEO_POSITION, currentPosition)
            putLong(VIDEO_ID, video.id)
            putLong(VIDEO_DURATION, video.duration)
            putString(VIDEO_URI, video.artUri.toString())

        }

        editor.commit()


        //val jSonString= GsonBuilder().create().toJson(lastplayedSong)
    }

    fun saveLastPlayedItem(context: Context, videoItem: PlayedVideoItem) {
        val lastPlayed = GsonBuilder().create().toJson(videoItem)

        val sharedPreference = context.getSharedPreferences(LASTPLAYED_VIDEO, Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.clear()
        editor.putString(LASTPLAYED_DATA, lastPlayed)
        editor.commit()

    }

    fun retrievingPlayedVideoId(context: Context): PlayedVideoItem {
        val editor = context.getSharedPreferences(LASTPLAYED_VIDEO, Context.MODE_PRIVATE)


        val lastPosition = editor.getLong(VIDEO_POSITION, 0L)
        val videoId = editor.getLong(VIDEO_ID, 0L)
        val videoDuration = editor.getLong(VIDEO_DURATION, 0L)
        val videoUriString = editor.getString(VIDEO_URI, "")
        val videoUri = Uri.parse(videoUriString)

        Log.d("TTT", "video position $lastPosition")
        Log.d("TTT", "video id $videoId")
        //need to clear the list after reading
        return PlayedVideoItem(videoId, videoUri, videoDuration, lastPosition)

    }

    fun retrievingLatPlayedVideo(context: Context): PlayedVideoItem? {

        val editor = context.getSharedPreferences(LASTPLAYED_VIDEO, Context.MODE_PRIVATE)

        val jsonString = editor.getString(LASTPLAYED_DATA, "")
        val typeToken = object : TypeToken<PlayedVideoItem>() {}.type

        return GsonBuilder().create().fromJson(jsonString, typeToken)

    }

    fun getProgressPercent(curretProgress: Long, tottalProgress: Long): Int {

        val current = curretProgress.toFloat()
        val tottel = tottalProgress.toFloat()

        val progress = (current.div(tottel)) * 100
        return progress.toInt()
    }
}