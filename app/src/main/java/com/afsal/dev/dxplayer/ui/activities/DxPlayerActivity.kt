package com.afsal.dev.dxplayer.ui.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.databinding.ActivityDxPlayerBinding
import com.afsal.dev.dxplayer.models.VideoSections.VideoItemModel
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout

/*import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback */

class DxPlayerActivity : AppCompatActivity() {

    private val TAG="DxPlayer"
    var isFullScreen=false
    var isLock=false
    private var check=false
    private val ad=4000
    private lateinit var bt_lockScreen:ImageView
    private lateinit var back_bt:ImageView
    private lateinit var tittle_text:TextView
    private lateinit var   bt_fullscreen: ImageView
    private lateinit var  currentVideo:VideoItemModel
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var handler: Handler
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


        currentVideo= intent.getParcelableExtra<VideoItemModel>(CoreUttiles.VIDEO)!!

        Log.d(TAG, "current Item  ${currentVideo.toString()}")
        handler= Handler(Looper.getMainLooper())

           setPlayer(currentVideo)

        back_bt.setOnClickListener { onBackPressed() }

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
            lockScreen(isLock)

        }


    }

    private fun setPlayer(videoItem:VideoItemModel) {
        exoPlayer=ExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()

        binding.videoView.apply {
            player=exoPlayer

            keepScreenOn=true
        }
        tittle_text.text=videoItem.tittle


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


        val mediaItem= MediaItem.fromUri(videoItem.artUri)  //vidioSourc
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()


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

    override fun onStop() {
        super.onStop()
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
    }

    private fun lockScreen(lock: Boolean) {

        val sec_mid=findViewById<LinearLayout>(R.id.sec_controlvid1)
        val sec_bottom=findViewById<LinearLayout>(R.id.sec_controlvid2)

        if (lock){
            sec_mid.visibility=View.INVISIBLE
            sec_bottom.visibility=View.INVISIBLE
        }else{
            sec_mid.visibility=View.VISIBLE
            sec_bottom.visibility=View.VISIBLE
        }

    }
}