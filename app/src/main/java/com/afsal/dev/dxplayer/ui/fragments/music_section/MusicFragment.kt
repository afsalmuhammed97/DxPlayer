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
import androidx.recyclerview.widget.RecyclerView
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
import com.google.android.exoplayer2.Player


class MusicFragment : BaseFragment<MusicFragmentBinding>(
    MusicFragmentBinding::inflate
) {

//    DialogBottomSheet().show( requireActivity().supportFragmentManager, "")

    private var musicService: MusicService? = null
    private val TAG = "MusicFragment"
    private var serviceConnected = false
    private lateinit var musicViewModel: MusicViewModel
    private lateinit var songsAdapter: SongsAdapter
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.music_fragment, container, false)
//    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MyBinder

            musicService = binder.currentService()
            Log.d(TAG, "MusicService connected $name")
            serviceConnected = true

            musicService!!.songsList.value = musicViewModel.musicList.value



            musicService!!.isPlayingLiveData.observe(viewLifecycleOwner, Observer { isplaying ->
                binding.miniPlayerLayout.visibility = View.VISIBLE

                binding.playerPlay.setImageResource(
                    if (isplaying == true)
                        R.drawable.ic_baseline_pause_circle_outline_24 else R.drawable.ic_baseline_play
                )

            })


            musicService!!.playbackState.observe(viewLifecycleOwner, Observer { playbackState ->
                Log.d("YYY", "play states  $playbackState")
                if (playbackState == PlaybackState.STATE_PLAYING) {
                    Log.d("YYY", "play state is playing ")
                }
            })

            musicService!!.currentSong.observe(viewLifecycleOwner, Observer { song ->
                Log.d(TAG, "song item  ${song.tittle}")
                updateTittle(song)
            })

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "MusicService disconnected $name")

        }

    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(requireContext(), MusicService::class.java)
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        requireActivity().stopService(intent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicViewModel = ViewModelProvider(this)[MusicViewModel::class.java]

        musicViewModel.loadAllMusicFiles()


        songsAdapter = SongsAdapter() { song ->

            Log.d(TAG, "selected song ${song.toString()}")
            musicService!!.setMediaItem(song)
            binding.miniPlayerLayout.visibility = View.VISIBLE

        }
        binding.apply {
            audioRv.layoutManager = GridLayoutManager(context, 3)
            audioRv.adapter = songsAdapter


        }
        binding.audioRv.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            //            var scrollDy = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //  Log.d(TAG,"new state  $newState")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

//                if (dy >0){
//                    binding.headerLayout.visibility=View.VISIBLE
//                }else{
//                    binding.headerLayout.visibility=View.INVISIBLE
//                }
//                if (recyclerView.scrollState==RecyclerView.SCROLL_STATE_SETTLING){
//                    Log.d(TAG,"recyclerview setting dy $dy dx $dx" )
//                }else if (recyclerView.scrollState==RecyclerView.SCROLL_STATE_SETTLING){
//                    Log.d(TAG,"recyclerview scrolling dy $dy dx $dx\"")
//                }else if (recyclerView.scrollState==RecyclerView.SCROLL_STATE_IDLE){
//                    Log.d(TAG,"recyclerview idieal     dy $dy dx $dx\"")
//                }
//                else{
//
//                }
            }
        })



        musicViewModel.musicList.observe(requireActivity(), Observer {

            Log.d(TAG, "musics ${it.toString()}")
            songsAdapter.differ.submitList(it)
        })

        binding.apply {
            playerPlay.setOnClickListener {
                musicService!!.playOrPauseSong()


            }
            playerNext.setOnClickListener {

                musicService!!.playNext()
            }
            playerPrev.setOnClickListener {
                musicService!!.playPrev()
            }

            closeBt.setOnClickListener {
                musicService!!.stopPlayer()
                binding.miniPlayerLayout.visibility = View.GONE
            }
            miniPlayerLayout.setOnClickListener {
                DialogBottomSheet().show(requireActivity().supportFragmentManager, "")


            }
        }


    }

    private fun updateTittle(song: MusicItem) {
        binding.titleText.text = song.tittle
        CoreUttiles.loadImageIntoView(song.imageUri, binding.musicImage, null, IMAGE_VIEW)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}