package com.afsal.dev.dxplayer.models.audioSectionsimport android.net.Uriimport android.os.Parcelableimport androidx.room.ColumnInfoimport androidx.room.Entityimport androidx.room.PrimaryKeyimport com.afsal.dev.dxplayer.utills.CoreUttiles.SONGS_TABLEimport kotlinx.parcelize.Parcelize@Parcelize@Entity(tableName = "songs_table")data class MusicItem(    @PrimaryKey val id: Long,    @ColumnInfo val tittle: String,    @ColumnInfo  val album: String,    @ColumnInfo  val artist: String,    @ColumnInfo  val duration: Long,    @ColumnInfo   val imageUri: String,    @ColumnInfo   val artUri: String,    @ColumnInfo val folderName: String):Parcelable