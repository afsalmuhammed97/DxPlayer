package com.afsal.dev.dxplayer.dataBaseimport android.content.Contextimport androidx.room.Databaseimport androidx.room.Roomimport androidx.room.RoomDatabaseimport com.afsal.dev.dxplayer.models.VideoSections.PlayedVideoItemimport com.afsal.dev.dxplayer.utills.CoreUttiles.WATCHED_VIDEO_DATA_BASE@Database( entities = [PlayedVideoItem::class], version = 1, exportSchema = true)abstract class RecentVideoDatabase:RoomDatabase() {    abstract fun videoHistoryDao():VideoHistoryDao    companion object{        private var INSTENCE:RecentVideoDatabase? =null        fun getVideoHistoryDatabase(context: Context):RecentVideoDatabase{            val tempInstance= INSTENCE            if (tempInstance!= null){                return tempInstance            }            synchronized(this){                val instance= Room.databaseBuilder(                    context.applicationContext,                    RecentVideoDatabase::class.java,                    WATCHED_VIDEO_DATA_BASE                ).build()                INSTENCE=instance                return  instance            }        }    }}