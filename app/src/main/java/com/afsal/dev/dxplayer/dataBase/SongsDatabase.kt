package com.afsal.dev.dxplayer.dataBaseimport android.content.Contextimport androidx.room.Databaseimport androidx.room.Roomimport androidx.room.RoomDatabaseimport com.afsal.dev.dxplayer.models.audioSections.MusicItemimport com.afsal.dev.dxplayer.models.audioSections.PlayListsimport com.afsal.dev.dxplayer.models.audioSections.PlayListWithSongsimport com.afsal.dev.dxplayer.utills.CoreUttiles.MUSIC_DATA_BASE@Database(    entities =    [  MusicItem::class,        PlayLists::class,        PlayListWithSongs::class],    version = 2, exportSchema = false)abstract class SongsDatabase : RoomDatabase() {    abstract fun musicDao(): MusicDao    companion object {        private var INSTANCE: SongsDatabase? = null        fun getDatabase(context: Context): SongsDatabase {            val tempInstance = INSTANCE            if (tempInstance != null) {                return tempInstance            }            synchronized(this) {                val instance = Room.databaseBuilder(                    context.applicationContext,                    SongsDatabase::class.java,                    MUSIC_DATA_BASE                ).build()                INSTANCE = instance                return instance            }        }    }}