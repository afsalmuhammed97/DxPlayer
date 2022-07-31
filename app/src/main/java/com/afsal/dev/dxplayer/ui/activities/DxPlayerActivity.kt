package com.afsal.dev.dxplayer.ui.activities


import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.postDelayed
import androidx.drawerlayout.widget.DrawerLayout
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.databinding.ActivityDxPlayerBinding

import com.afsal.dev.dxplayer.models.VideoSections.VideoItemModel
import com.afsal.dev.dxplayer.ui.fragments.DialogBottomSheet
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.util.Util
import com.google.android.material.navigation.NavigationView
import java.util.*
import kotlin.math.abs


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
    private var brightness =0F
    private var volume=0F
    private val audioTracks=ArrayList<String>()

    private lateinit var audioManager: AudioManager
    private lateinit var detector:GestureDetectorCompat
    private lateinit var radioGroupAudio: RadioGroup
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var dialogDrawer:NavigationView
    private lateinit var bt_lockScreen:ImageView
    private lateinit var back_bt:ImageView
    private lateinit var checkBox: CheckBox
    private lateinit var skippCountText:TextView
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
           radioGroupAudio=findViewById(R.id.radio_group)
          checkBox=findViewById(R.id.checkBox)
           skippCountText=findViewById(R.id.skipp_count_text)

            detector=GestureDetectorCompat(this,SwipeListener())

        currentVideo= intent.getParcelableExtra<VideoItemModel>(CoreUttiles.VIDEO)!!

         audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        Log.d(TAG, "current Item  ${currentVideo.toString()}")
        handler= Handler(Looper.getMainLooper())

//         initPlayer()
        automateScreenMode()
        back_bt.setOnClickListener {
            onBackPressed()
            this.finish()
        }
        track_bt.setOnClickListener {

           drawerLayout.openDrawer(Gravity.END)

             getAudioTracks()
           //  setScreenBrightness(23)


        }


        checkBox.setOnClickListener {


            if (checkBox.isChecked){
                trackSelector.parameters=DefaultTrackSelector.ParametersBuilder(this)
                    .setRendererDisabled(C.TRACK_TYPE_VIDEO,false).build()
            }else{
                trackSelector.parameters=DefaultTrackSelector.ParametersBuilder(this)
                    .setRendererDisabled(C.TRACK_TYPE_VIDEO,true).build()
            }
        }


      bt_fullscreen.setOnClickListener {

          if (!isFullScreen){

              bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                  R.drawable.ic_baseline_fullscreen_exit))
              binding.videoView.resizeMode = AspectRatioFrameLayout. RESIZE_MODE_FILL

              requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
//              val params = binding.videoView.layoutParams
//              params.width = params.width
//              params.height = ( 200 * applicationContext.resources.displayMetrics.density).toInt()



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
             //  Log.d(TAG,"drawer slided")
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

        binding.videoView.setOnTouchListener{_,event->

                 detector.onTouchEvent(event)
            return@setOnTouchListener false
        }

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event != null) {
            Log.d("EEE",event.action.toString())
            CoreUttiles
        }
        return super.onTouchEvent(event)
    }
//        return   if(event?.let { detector.onTouchEvent(it) } == true){
//                  true
//                }else{
//
//                  super.onTouchEvent(event)
//        }





      private fun automateScreenMode(){
          val displayMetrics = DisplayMetrics()
          this.windowManager.defaultDisplay.getMetrics(displayMetrics)


          if (currentVideo.width >=displayMetrics.widthPixels){
              //landscape
              bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                  R.drawable.ic_baseline_fullscreen_exit))
              binding.videoView.resizeMode = AspectRatioFrameLayout. RESIZE_MODE_FILL

              requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
              isFullScreen=true

          }else{
              //portrait
              bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext
                  ,R.drawable.ic_fullscreen_icon))

              requestedOrientation=(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
              binding.videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
              isFullScreen=false
          }
      }

    private fun initPlayer() {
        trackSelector= DefaultTrackSelector(this)
        trackSelector.setParameters( trackSelector.parameters.buildUpon().setPreferredAudioLanguage("eng"))
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


        exoPlayer.addListener(object : Player.Listener {
            override fun onTracksChanged(tracks: Tracks) {
                // Update UI using current tracks.
            }
        })




        tittle_text.text=currentVideo.tittle


        exoPlayer.addListener(object :Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)

                if (playbackState ==Player.STATE_BUFFERING){
                 //binding.progressBar.visibility= View.VISIBLE

                }else if ( playbackState == Player.STATE_READY){
                  //  binding.progressBar.visibility= View.GONE

                }

                if (!exoPlayer.playWhenReady){
                    handler.removeCallbacks(updateProgressAction)
                }else{
                    onProgress()
                }
            }

            override fun onTracksChanged(tracks: Tracks) {

                super.onTracksChanged(tracks)

            }


        })





    }

    private fun getAudioTracks(){

           audioTracks.clear()

        for (i in 0 until exoPlayer.currentTrackGroups.length) {

            if (exoPlayer.currentTrackGroups.get(i)
                    .getFormat(0).selectionFlags == C.SELECTION_FLAG_DEFAULT) {         //SELECTION_FLAG_DEFAULT

                Log.d(TAG,"TracksList ${exoPlayer.currentTrackGroups.get(i).getFormat(0).language.toString()}")
                audioTracks.add(Locale(exoPlayer.currentTrackGroups.get(i).getFormat(0).language.toString()).displayLanguage)

            }

        }
        Log.d(TAG,"audio tracks ${audioTracks.toString()}")


          /* creating radiobutton for track selection         */
        creatingRadioButtons(audioTracks)



            radioGroupAudio.setOnCheckedChangeListener { group, checkedId ->

                val selectedButton=findViewById<RadioButton>(checkedId)
                val trackPosition= audioTracks.indexOf(selectedButton.text.toString())


                trackSelector.setParameters(trackSelector.buildUponParameters().setPreferredAudioLanguage(audioTracks[trackPosition]))
            }







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
        if (brightness !=0F ) setScreenBrightness(brightness.toInt())


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


private fun creatingRadioButtons(audioTracksList:ArrayList<String>) {


    if (Build.VERSION.SDK_INT >= 21) {}
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled)
            ), intArrayOf(
                Color.BLACK,  // disabled
                Color.WHITE // enabled

            )
        )




           radioGroupAudio.removeAllViews()
          radioGroupAudio.orientation=LinearLayout.VERTICAL
    for (i in 0 until audioTracksList.size) {
        val radioButton = RadioButton(this)
        radioButton.setTextColor(Color.WHITE)
        radioButton.buttonTintList = colorStateList
        radioButton.id = View.generateViewId()

        radioButton.text = audioTracksList[i]
        val params =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        radioButton.layoutParams = params
        radioGroupAudio.addView(radioButton)
        radioButton.defaultFocusHighlightEnabled=true
    }

}


    inner class SwipeListener:GestureDetector.SimpleOnGestureListener(){
      private val SWIP_THRESHOLD=120
        private val DOWN_SWIP_THERSHOLD=80
        override fun onScroll(
            douwnEvent: MotionEvent?,
            moveEvent: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
         val screenWidth=Resources.getSystem().displayMetrics.widthPixels


            val diffX= moveEvent?.x?.minus(douwnEvent!!.x) ?:0.0F
             val diffY=moveEvent?.y?.minus(douwnEvent!!.y) ?:0.0F
            Log.d("DDD","fiffy $diffY")
            //to control volume and brightness
            if (abs(distanceX) < abs(distanceY)  ){

                if (abs(diffY) > DOWN_SWIP_THERSHOLD){
               if (douwnEvent!!.x <screenWidth/2){


                 //  if (douwnEvent?.action== MotionEvent.ACTION_DOWN ) binding.brightnessLayout.visibility=View.VISIBLE
                //left side event

                val increase=distanceY >= 0
                val newValue= if (increase) brightness +0.25 else brightness -0.25

                   if(newValue in 0F ..30F) {
                       brightness = newValue.toFloat()

                       this@DxPlayerActivity.setScreenBrightness(brightness.toInt())

                   }





               }else{
                  // right side event

                   val maxVolume= audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

                   val increase=distanceY >= 0
                   val newValue= if (increase) volume +0.25 else volume -0.25

                   if(newValue in 0F ..maxVolume.toFloat()) {
                       volume = newValue.toFloat()

                       audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume.toInt(),0)

                       updateVolumeProgress(volume,maxVolume)

                       Log.d("Volume","volume ${volume.toInt()}")


                         //need to implement volume progress on left side
                   }





               }

            }
            }else{
                Log.d("DDD","diffx   $diffX")
                var seek=5000+abs(diffX).toInt()



                         Log.d("DDD","diffx+seek   $seek")

                  if ( abs(diffX) >SWIP_THRESHOLD){

                      skippCountText.visibility=View.VISIBLE

                  if (diffX >0){

                      binding.skipRight.visibility=View.VISIBLE
                      val tex= CoreUttiles.durationToHour((exoPlayer.currentPosition +seek))
                      skippCountText.text="[ $tex ]"
                      exoPlayer.seekTo(exoPlayer.currentPosition +seek)


                  }else{
                      exoPlayer.seekTo(exoPlayer.currentPosition -seek)
                      val tex=CoreUttiles.durationToHour((exoPlayer.currentPosition -seek))
                      skippCountText.text="[ $tex ]"
                      binding.skipLeft.visibility=View.VISIBLE

                  }
                  }
                binding.skippCountText.postDelayed( {
                    run {

                        binding.apply {
                            skippCountText.visibility=View.INVISIBLE
                            skipLeft.visibility=View.INVISIBLE
                            skipRight.visibility=View.INVISIBLE
                        }
                        this@DxPlayerActivity.hideControls(false,true)
                    }
                }, 1600);




            }

            return super.onScroll(douwnEvent, moveEvent, distanceX, distanceY)
        }

        private fun updateVolumeProgress(value: Float,max:Int) {

            binding.volumeLayout.visibility=View.VISIBLE

            val p=(value.div(max))*100
            binding.volumeProgress.progress=p.toInt()
            Log.d("Volume","volume progerss ${p})")


            binding.volumeLayout.postDelayed( {
                run {
                    binding.volumeLayout.visibility=View.INVISIBLE
                    this@DxPlayerActivity.hideControls(false,true)
                }
            }, 2000);

        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.d("DDD","single tap${e?.action}")
                  if (e?.action ==1){
                      this@DxPlayerActivity.hideControls(false,false)
                  }

            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.d("DDD","double tap${e?.action}")

            return super.onDoubleTap(e)
        }





    }
    fun setScreenBrightness(value:Int){


        val bright=value.toFloat()
        val p=(bright.div(30))*100
        Log.d(TAG,"brghtness b $p")
         binding.brightnessProgress.progress=p.toInt()
        // binding.brightnessLayout.visibility=View.VISIBLE


        val d =1.0f/30
        val layoutParams=this.window.attributes

        layoutParams.screenBrightness=d * value
         this.window.attributes=layoutParams

        binding.brightnessLayout.visibility=View.VISIBLE

        binding.brightnessLayout.postDelayed( {
            run {
                binding.brightnessLayout.visibility=View.INVISIBLE
                hideControls(false,true)
            }
        }, 2000);


    }


}

