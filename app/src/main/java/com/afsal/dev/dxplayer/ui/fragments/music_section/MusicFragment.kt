package com.afsal.dev.dxplayer.ui.fragments.music_section

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.adapters.SongsAdapter
import com.afsal.dev.dxplayer.databinding.MusicFragmentBinding
import com.afsal.dev.dxplayer.models.audioSections.MusicItem
import com.afsal.dev.dxplayer.ui.fragments.BaseFragment
import com.afsal.dev.dxplayer.ui.fragments.DialogBottomSheet
import com.afsal.dev.dxplayer.ui.services.MusicService
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.utills.CoreUttiles.IMAGE_VIEW
import com.afsal.dev.dxplayer.view_models.MusicViewModel


class MusicFragment : BaseFragment<MusicFragmentBinding>(
    MusicFragmentBinding::inflate
) {


    private var musicService: MusicService? = null
    private val TAG = "MusicFragment"
    private var serviceConnected = false
    private lateinit var musicViewModel: MusicViewModel
    private lateinit var songsAdapter: SongsAdapter


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MyBinder

            musicService = binder.currentService()
            Log.d(TAG, "MusicService connected $name")
            serviceConnected = true




            musicViewModel.musicList.observe(viewLifecycleOwner, Observer {
                musicService!!.songsList.value=it
            })




//            musicService!!.isPlayingLiveData.observe(viewLifecycleOwner, Observer { isplaying ->
//
//            //   binding.miniPlayerLayout.visibility = View.VISIBLE
//
//                binding.playerPlay.setImageResource(
//                    if (isplaying == true)
//                        R.drawable.ic_baseline_pause_circle_outline_34 else R.drawable.ic_baseline_play
//                )
//            })


            musicService!!.playbackState.observe(viewLifecycleOwner, Observer { playbackState ->
                Log.d(TAG, "play states  $playbackState")
                if (playbackState == PlaybackState.STATE_PLAYING) {

                }
            })

//            musicService!!.currentSong.observe(viewLifecycleOwner, Observer { song ->
//                Log.d(TAG, "song item  ${song.tittle}")
//             //   currentSong = song
//                updateTittle(song)
//            })



        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "MusicService disconnected $name")

        }

    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(requireContext(), MusicService::class.java)
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
       // requireActivity().stopService(intent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicViewModel = ViewModelProvider(this)[MusicViewModel::class.java]

        musicViewModel.loadAllMusicFiles()


        songsAdapter = SongsAdapter() { song ->

            Log.d(TAG, "selected song ${song.toString()}")
            musicService!!.setMediaItem(song)


        }
        binding.apply {
            audioRv.layoutManager = GridLayoutManager(context, 3)
            audioRv.adapter = songsAdapter


        }




        musicViewModel.musicList.observe(requireActivity(), Observer {

            Log.d(TAG, "musics ${it.toString()}")
            songsAdapter.differ.submitList(it)
        })

        binding.apply {

            
            btFav.setOnClickListener { navigateToPlayListFragment() }
            btPlaylist.setOnClickListener { navigateToPlayListFragment() }
            btResent.setOnClickListener { navigateToPlayListFragment() }
        }


    }

    private fun navigateToPlayListFragment(){
        findNavController().navigate(R.id.action_navigation_music_to_playListFragment)
    }

//    private fun updateTittle(song: MusicItem) {
//        binding.titleText.text = song.tittle
//        CoreUttiles.loadImageIntoView(song.imageUri, binding.musicImage, null, IMAGE_VIEW)
//    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//        // TODO: Use the ViewModel
//    }
//
//    private fun updateSongProgress(currentPosition: Long, duration: Long) {
//        val p = currentPosition.toFloat()
//        val d = duration.toFloat()
//        val progress = p.div(d) * 100
//        Log.d(TAG, "current progress $progress")
//        binding.exoProgress.progress = progress.toInt()
//    }
}