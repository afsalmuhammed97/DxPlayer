package com.afsal.dev.dxplayer.ui.servicesimport android.app.Serviceimport android.content.Intentimport android.media.session.PlaybackStateimport android.os.Binderimport android.os.Handlerimport android.os.IBinderimport android.os.Looperimport android.support.v4.media.session.MediaSessionCompatimport android.support.v4.media.session.PlaybackStateCompatimport android.util.Logimport androidx.lifecycle.LiveDataimport androidx.lifecycle.MutableLiveDataimport com.afsal.dev.dxplayer.models.audioSections.MusicItemimport com.afsal.dev.dxplayer.utills.CoreUttilesimport com.google.android.exoplayer2.ExoPlayerimport com.google.android.exoplayer2.MediaItemimport com.google.android.exoplayer2.Playerimport com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_AUTOimport com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_SEEKimport com.google.android.exoplayer2.Timelineimport com.google.android.exoplayer2.trackselection.DefaultTrackSelectorclass MusicService : Service() {    private val TAG = "MusicService"    private var currentIndex = 0    private val _currentSong = MutableLiveData<MusicItem>()    val currentSong: LiveData<MusicItem> = _currentSong    var songsList = MutableLiveData<List<MusicItem>>()    private val _repeatMode=MutableLiveData<Int>()    val repeatMode:LiveData<Int> =_repeatMode    private val _isPlayingLiveData = MutableLiveData<Boolean>()    val isPlayingLiveData: LiveData<Boolean> = _isPlayingLiveData    private val currentPlayBackPosition = MutableLiveData<Long>()    val currentPosition: LiveData<Long> = currentPlayBackPosition    private val _playbackState = MutableLiveData<Int>()    val playbackState: LiveData<Int> = _playbackState     private  var suffleMode=false    private lateinit var handler: Handler    private val mSeekbarUpdateHandler = Handler()    private lateinit var mediaSession: MediaSessionCompat    private lateinit var exoPlayer: ExoPlayer    private lateinit var trackSelector: DefaultTrackSelector    override fun onBind(intent: Intent?): IBinder? {        mediaSession = MediaSessionCompat(baseContext, TAG)        return this.MyBinder()    }    override fun onDestroy() {        super.onDestroy()        if (exoPlayer.isPlaying){            exoPlayer.pause()            exoPlayer.release()        }    }    override fun onStart(intent: Intent?, startId: Int) {        super.onStart(intent, startId)    }    private fun initPlayer() {        //  trackSelector= DefaultTrackSelector(this)        exoPlayer = ExoPlayer.Builder(this)            .setSeekBackIncrementMs(5000)            .setSeekForwardIncrementMs(5000)            .build()        exoPlayer.addListener(this.playerEvents())    }    override fun onCreate() {        super.onCreate()        initPlayer()        handler = Handler(Looper.getMainLooper())    }    fun stopPlayer() {        if (exoPlayer.isPlaying) exoPlayer.pause()        exoPlayer.stop()    }    fun setMediaItem(song: MusicItem) {        currentIndex = songsList.value?.indexOf(song) ?: 0        val mediaItem = MediaItem.fromUri(song.artUri)        exoPlayer.setMediaItem(mediaItem)        exoPlayer.prepare()        exoPlayer.play()        setMediaItemsToPlayer()    }    fun playOrPauseSong() {        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()    }    fun playNext() {        if (songsList.value?.size?.minus(1) == currentIndex)            setMediaItem(songsList.value!![0])        else            setMediaItem(songsList.value!![currentIndex + 1])    }    fun playPrev() {        if (currentIndex == 0)            setMediaItem(songsList.value!![songsList.value!!.size.minus(1)])        else            setMediaItem(songsList.value!![currentIndex - 1])    }    fun seekToPosition(position: Float) {        val lastPosition = (position.div(100)) * currentSong.value!!.duration.toFloat()        exoPlayer.seekTo(lastPosition.toLong())        Log.d("DDD", "exo total seek ${CoreUttiles.durationToHour(lastPosition.toLong())}")    }    fun setSuffleMode(){        exoPlayer.shuffleModeEnabled = !suffleMode    }    fun setRepeatMode(){        if (exoPlayer.repeatMode==Player.REPEAT_MODE_OFF){            exoPlayer.repeatMode=Player.REPEAT_MODE_ONE        }else if (exoPlayer.repeatMode==Player.REPEAT_MODE_ONE){            exoPlayer.repeatMode=Player.REPEAT_MODE_ALL        }else{            exoPlayer.repeatMode=Player.REPEAT_MODE_OFF        }    }    private fun setMediaItemsToPlayer() {        exoPlayer.clearMediaItems()        val mediaItems = mutableListOf<MediaItem>()       Log.d("YYY","CurrentIndex $currentIndex")       Log.d("YYY","songList ${songsList.value}  size ${songsList.value!!.size}")        for (i in currentIndex until songsList.value!!.size.minus(1)) {            mediaItems.add(MediaItem.fromUri(songsList.value!![i].artUri))        }        exoPlayer.setMediaItems(mediaItems)    }    inner class MyBinder : Binder() {        fun currentService(): MusicService {            return this@MusicService        }    }    inner class playerEvents() : Player.Listener {        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {            if (playbackState == PlaybackStateCompat.STATE_SKIPPING_TO_NEXT) {                Log.d("SSS", "playback skipping to next")            } else if (playbackState == PlaybackState.STATE_SKIPPING_TO_NEXT) {                Log.d("SSS", "playback skipping to next2")            }            when (playbackState) {                PlaybackState.STATE_PLAYING -> {}                PlaybackState.STATE_PAUSED -> {}                PlaybackState.STATE_BUFFERING -> {}                PlaybackState.STATE_PLAYING -> {}            }            super.onPlayerStateChanged(playWhenReady, playbackState)        }        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {              Log.d("SSS","suffleMode $shuffleModeEnabled")            super.onShuffleModeEnabledChanged(shuffleModeEnabled)        }        override fun onPlaybackStateChanged(playbackState: Int) {            _playbackState.postValue(playbackState)            super.onPlaybackStateChanged(playbackState)        }        override fun onSeekBackIncrementChanged(seekBackIncrementMs: Long) {            Log.d("SSS", "seek            changes $seekBackIncrementMs")            super.onSeekBackIncrementChanged(seekBackIncrementMs)        }        override fun onSeekForwardIncrementChanged(seekForwardIncrementMs: Long) {            Log.d("SSS", "seek  forward changes $seekForwardIncrementMs")            super.onSeekForwardIncrementChanged(seekForwardIncrementMs)        }        override fun onIsPlayingChanged(isPlaying: Boolean) {            Log.d("SSS", "    isPlaying changed $isPlaying")            this@MusicService._isPlayingLiveData.value = isPlaying            this@MusicService._currentSong.value = songsList.value!![currentIndex]          //  this@MusicService.updateCurrentSongData(songsList.value!![currentIndex])            this@MusicService.updateProgress(isPlaying)            super.onIsPlayingChanged(isPlaying)        }        override fun onRepeatModeChanged(repeatMode: Int) {            Log.d("RRR","repeat mode $repeatMode")            _repeatMode.postValue(repeatMode)            super.onRepeatModeChanged(repeatMode)        }        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {            when (reason) {                MEDIA_ITEM_TRANSITION_REASON_AUTO -> {                    currentIndex += 1                    Log.d("SSSS", "medeaItem changed $mediaItem       reason auto $reason")                    val song=songsList.value!![currentIndex]                    this@MusicService._currentSong.value = songsList.value!![currentIndex]                  //  this@MusicService.updateCurrentSongData(song)                }                MEDIA_ITEM_TRANSITION_REASON_SEEK -> {                    Log.d("SSSS", "medeaItem changed $mediaItem       reason $reason")                }            }            super.onMediaItemTransition(mediaItem, reason)        }        override fun onEvents(player: Player, events: Player.Events) {            Log.d("EEEE", "player events  ${events.toString()}")            super.onEvents(player, events)        }        override fun onTimelineChanged(timeline: Timeline, reason: Int) {            Log.d("TTT", "onTimeline  $timeline   reason $reason ")            super.onTimelineChanged(timeline, reason)        }    }    private fun updateProgress(isPlaying: Boolean) {        if (isPlaying)            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0)        else            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar)    }    private val mUpdateSeekbar: Runnable = object : Runnable {        override fun run() {            currentPlayBackPosition.postValue(exoPlayer.currentPosition)            mSeekbarUpdateHandler.postDelayed(this, 1000)        }    }        //_currentSongData.value=songItem//        apply {//            this.id=song.id//            this.tittle=song.tittle//            this.album=song.album//            this.artUri=song.artUri//            this.artist=song.artist//            this.duration=song.duration//            this.folderName=song.folderName//            this.imageUri=song.imageUri//            this.currentPosition= if (exoPlayer.isPlaying) exoPlayer.currentPosition else 0L//        }    }