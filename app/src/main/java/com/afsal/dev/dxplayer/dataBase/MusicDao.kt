package com.afsal.dev.dxplayer.dataBaseimport androidx.lifecycle.LiveDataimport androidx.room.*import com.afsal.dev.dxplayer.models.audioSections.MusicItemimport com.afsal.dev.dxplayer.models.audioSections.PlayListWithSongsimport com.afsal.dev.dxplayer.models.audioSections.PlayLists@Daointerface MusicDao {    @Insert(onConflict = OnConflictStrategy.REPLACE)    suspend fun createPlayList(playLists: PlayLists)         //pass the playList name    @Insert(onConflict = OnConflictStrategy.REPLACE)              //   fun 1    suspend fun insertSongIntoSongsTable(song: MusicItem)          //insert single song into songTable    //fun 1 and 2 should call to add a song to playlist    @Insert(onConflict = OnConflictStrategy.REPLACE)                             //fun 2    suspend fun insertSongsIntoPlayList(playListWithSongs: PlayListWithSongs)      //to add a song to playList    //to get all playlist names    @Transaction    @Query("SELECT * FROM play_list_table ORDER BY playListName ASC ")    fun getAllPlayList(): LiveData<List<PlayLists>>    //to get all song id s included in a playList    @Transaction    @Query("SELECT * FROM play_list_song_table WHERE PlayListName = :playListName ")    suspend fun getAllSongsInPlayList(playListName: String): List<PlayListWithSongs>    @Transaction    @Query("SELECT * FROM songs_table WHERE id = :idList ") //need to cross check    suspend fun getAllSongsInPlaylist(idList: List<Long>): List<MusicItem>    @Query("DELETE FROM play_list_song_table WHERE id =:songId AND PlayListName =:playListName")    suspend fun deleteSongFromPlayList(songId:Long,playListName: String)//    @Query("SELECT * FROM user_table ORDER BY  id ASC")////    fun   readAllData(): LiveData<List<User>>}