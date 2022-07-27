package com.afsal.dev.dxplayer.ui.activities


import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.databinding.ActivityDxPlayerBinding
import com.afsal.dev.dxplayer.models.VideoSections.VideoItemModel
import com.afsal.dev.dxplayer.ui.fragments.DialogBottomSheet
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.DefaultTrackNameProvider
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import com.google.android.material.navigation.NavigationView
import java.util.*


/*import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback */

class DxPlayerActivity : AppCompatActivity() {

    private val TAG="DxPlayer"
    var isFullScreen=false
    var isLock=false
    private var check=false
    private val ad=4000
    private var playWhenReady=false
    private var currentItem=0
    private var playBackPosition= 0L


    private val audioTracks=ArrayList<String>()
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var dialogDrawer:NavigationView
    private lateinit var bt_lockScreen:ImageView
    private lateinit var back_bt:ImageView
    private lateinit var tittle_text:TextView
    private lateinit var   bt_fullscreen: ImageView
    private lateinit var track_bt:ImageView
    private lateinit var  currentVideo:VideoItemModel
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var handler: Handler
    private lateinit var trackSelector:DefaultTrackSelector
    private lateinit var binding: ActivityDxPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityDxPlayerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

            bt_fullscreen=findViewById(R.id.bt_fullscreen)
            bt_lockScreen=findViewById(R.id.exo_lock)
           back_bt=findViewById(R.id.exo_back)
           tittle_text=findViewById(R.id.title_text)
           track_bt=findViewById(R.id.exo_audio_track)
          dialogDrawer=findViewById(R.id.dx_player_drawer)
          drawerLayout=findViewById(R.id.drawer_layout)


        currentVideo= intent.getParcelableExtra<VideoItemModel>(CoreUttiles.VIDEO)!!

        Log.d(TAG, "current Item  ${currentVideo.toString()}")
        handler= Handler(Looper.getMainLooper())

//         initPlayer()
        back_bt.setOnClickListener { onBackPressed() }
        track_bt.setOnClickListener {                // showAudioTracks()

            drawerLayout.openDrawer(Gravity.END)
         //   hideControls(true)
               getAudioTracks()

        }





      bt_fullscreen.setOnClickListener {

          if (!isFullScreen){

              bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                  R.drawable.ic_baseline_fullscreen_exit))
              binding.videoView.resizeMode = AspectRatioFrameLayout. RESIZE_MODE_FILL

              requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
          }else{
              bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext
                  ,R.drawable.ic_fullscreen_icon))

              requestedOrientation=(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                           binding.videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
          }

          isFullScreen =! isFullScreen
      }

        bt_lockScreen.setOnClickListener {
            if (! isLock){
                bt_lockScreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                   R.drawable.ic_baseline_lock))

            }else{
                bt_lockScreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.ic_baseline_lock_open_24))

            }
            isLock =!isLock
           // lockScreen(isLock)
            hideControls(isLock,false)

        }







        binding.drawerLayout.addDrawerListener(object :
            DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
               Log.d(TAG,"drawer slided")
            }

            override fun onDrawerOpened(drawerView: View) {
                 hideControls(false, allControls = true)
                Log.d(TAG,"drawer opened")
            }

            override fun onDrawerClosed(drawerView: View) {
                hideControls(false, allControls = false)
            }

            override fun onDrawerStateChanged(newState: Int) {
                Log.d(TAG,"drawer state changed")
            }
        })

        binding.drawerLayout.setScrimColor(Color.TRANSPARENT)


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        Log.d("Event",event.toString())
        return super.onTouchEvent(event)

    }


    private fun initPlayer() {
        trackSelector= DefaultTrackSelector(this)

        val mediaItem= MediaItem.fromUri(currentVideo.artUri)


        exoPlayer=ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()

                binding.videoView.apply {
                    player=exoPlayer
                    keepScreenOn=true }
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.playWhenReady=playWhenReady
        exoPlayer.seekTo(currentItem,playBackPosition)
        exoPlayer.prepare()
        exoPlayer.play()


       // Log.d(TAG,"Tracks  ${ exoPlayer.currentTrackGroups.get().getFormat().language}")



        //track selection



//            Log.d(TAG,"Tracks  ${ exoPlayer.currentTrackGroups.get(0).getFormat(0).language}")

//
//
//                audioTracks.add(
//                    Locale(exoPlayer.currentTrackGroups.get(i).getFormat(0).language.toString()).displayLanguage)
//            }
//            audioTracks.add(
              //  Locale(exoPlayer.currentTrackGroups.get(i).getFormat(0).language.toString()).displayLanguage)

//            Log.d(TAG,"audioTracks  ${audioTracks.toString()}")



        tittle_text.text=currentVideo.tittle


        exoPlayer.addListener(object :Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)

                if (playbackState ==Player.STATE_BUFFERING){
                 //binding.progressBar.visibility= View.VISIBLE

                }else if ( playbackState == Player.STATE_READY){
                    binding.progressBar.visibility= View.GONE
                }

                if (!exoPlayer.playWhenReady){
                    handler.removeCallbacks(updateProgressAction)
                }else{
                    onProgress()
                }


            }
        })





    }

    fun getAudioTracks(){

        for (i in 0 until exoPlayer.currentTrackGroups.length) {

            if (exoPlayer.currentTrackGroups.get(i)
                    .getFormat(0).selectionFlags == C.SELECTION_FLAG_DEFAULT) {

                audioTracks.add(Locale(exoPlayer.currentTrackGroups.get(i).getFormat(0).language.toString()).displayLanguage)

            }

        }
        Log.d(TAG,"audio tracks ${audioTracks.toString()}")
    }



    private val updateProgressAction = Runnable {   onProgress() }
     fun onProgress(){

        val player= exoPlayer
        val position:Long =if (player == null) 0 else exoPlayer .currentPosition

        handler.removeCallbacks(updateProgressAction)

        val playbackState= if (player != null) Player.STATE_IDLE  else  exoPlayer.playbackState

         if (playbackState !=Player.STATE_IDLE && playbackState != Player.STATE_ENDED){

             var delayMs:Long
             if (player.playWhenReady && playbackState == Player.STATE_READY){
                 delayMs= 1000 -position %1000
                 if (delayMs <200){
                     delayMs +=1000
                 }
             }else{
                 delayMs =1000
             }

             if ((ad-3000 <= position &&  position <= ad) && !check){

                 check=true
               ///  initAd()
             }
             handler.postDelayed(updateProgressAction,delayMs)

    }
}

        private fun showAudioTracks(){
            DialogBottomSheet().show(supportFragmentManager,"")
        }

  //  var rewardedInterstitialAd: RewardedInterstitialAd?=null
//  private fun initAd() {
//        if (rewardedInterstitialAd !=null)  return
//
//        MobileAds.initialize(this)
//            RewardedInterstitialAd.load(this,"",AdRequest.Builder().build(),object :RewardedInterstitialAdLoadCallback(){
//
//                override fun onAdLoaded(p0: RewardedInterstitialAd) {
//                    rewardedInterstitialAd=p0
//                    rewardedInterstitialAd!!.fullScreenContentCallback =object :
//                        FullScreenContentCallback(){
//
//
//                        override fun onAdDismissedFullScreenContent() {
//                            super.onAdDismissedFullScreenContent()
//
//                            exoPlayer.playWhenReady=true
//                            rewardedInterstitialAd=null
//
//                            check=false
//                        }
//
//                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                            Log.d("WatchScreen_AD",p0.message)
//                        }
//
//
//
//                        override fun onAdShowedFullScreenContent() {
//
//                            handler.removeCallbacks(updateProgressAction)
//                        }
//                    }
//                    val sec_ad_countdown=findViewById<LinearLayout>(R.id.sect_ad_countdown)
//                    val tx_ad_countdown=findViewById<TextView>(R.id.tx_ad_countdown)
//                    sec_ad_countdown.visibility=View.VISIBLE
//
//                    object : CountDownTimer(4000,1000){
//                        @SuppressLint("SetTextI18n")
//                        override fun onTick(p0: Long) {
//                            tx_ad_countdown.text="Ad in${p0/1000}"
//                        }
//
//                        override fun onFinish() {
//                            sec_ad_countdown.visibility=View.GONE
//                            rewardedInterstitialAd!!.show(this@DxPlayerActivity) {}
//                        }
//
//                    }.start()
//
//                }
//
//                override fun onAdFailedToLoad(p0: LoadAdError) {
//                    rewardedInterstitialAd=null
//
//                }
//            })
//
//
//
//    }

    override fun onBackPressed() {

        if (isLock) return
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){

            bt_fullscreen.performClick()
        }
        else    super.onBackPressed()


    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >23 ||exoPlayer == null){
            initPlayer()
        }

    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <23 ||exoPlayer == null){
            initPlayer()
        }

    }
    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23){
            releasePlayer()
        }

        exoPlayer.stop()
    }


    override fun onPause() {
        super.onPause()
//        if (Util.SDK_INT <=23){
//            releasePlayer()
//        }
        exoPlayer.pause()
    }

    private fun releasePlayer() {
        exoPlayer.let {

            playWhenReady=exoPlayer.playWhenReady
            currentItem=exoPlayer.currentMediaItemIndex
            playBackPosition=exoPlayer.currentPosition
            exoPlayer.stop()
            exoPlayer.release()

        }
       // exoPlayer=null
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }


//    private fun lockScreen(lock: Boolean) {
//
//        val sec_mid=findViewById<LinearLayout>(R.id.sec_controlvid1)
//        val sec_bottom=findViewById<LinearLayout>(R.id.sec_controlvid2)
//
//        if (lock){
//            sec_mid.visibility=View.INVISIBLE
//            sec_bottom.visibility=View.INVISIBLE
//        }else{
//            sec_mid.visibility=View.VISIBLE
//            sec_bottom.visibility=View.VISIBLE
//        }
//
//    }

    private fun hideControls(lock: Boolean,allControls:Boolean){
       val top_layout=findViewById<LinearLayout>(R.id.title_layout)
        val sec_mid=findViewById<LinearLayout>(R.id.sec_controlvid1)
        val sec_bottom=findViewById<LinearLayout>(R.id.sec_controlvid2)

        if (lock){
            top_layout.visibility=View.INVISIBLE
            sec_mid.visibility=View.INVISIBLE
            sec_bottom.visibility=View.INVISIBLE

        }else if (allControls){
            sec_mid.visibility=View.INVISIBLE
            top_layout.visibility=View.INVISIBLE
            sec_bottom.visibility=View.INVISIBLE
            bt_lockScreen.visibility=View.INVISIBLE
        }
        else{
            sec_mid.visibility=View.VISIBLE
            top_layout.visibility=View.VISIBLE
            sec_bottom.visibility=View.VISIBLE
            bt_lockScreen.visibility=View.VISIBLE
        }



    }
}

class   TrackSelectionDialog {

}
