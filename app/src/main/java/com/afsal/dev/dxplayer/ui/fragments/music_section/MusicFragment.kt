package com.afsal.dev.dxplayer.ui.fragments.music_section

import android.content.*
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.afsal.dev.dxplayer.services.MusicService
import com.afsal.dev.dxplayer.utills.CoreUttiles.FAVOURITE
import com.afsal.dev.dxplayer.view_models.MusicViewModel


class MusicFragment : BaseFragment<MusicFragmentBinding>(
    MusicFragmentBinding::inflate
) {


    private var musicService: MusicService? = null
    private val TAG = "MusicFragment"
    private var serviceConnected = false
    private lateinit var musicViewModel: MusicViewModel
    private lateinit var songsAdapter: SongsAdapter

    private var musicList = mutableListOf<MusicItem>()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MyBinder

            musicService = binder.currentService()
            Log.d(TAG, "MusicService connected $name")
            serviceConnected = true






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
       this.setHasOptionsMenu(true)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        musicViewModel = ViewModelProvider(requireActivity())[MusicViewModel::class.java]

        musicViewModel.loadAllMusicFiles()




        songsAdapter = SongsAdapter() { song,idex ->

            Log.d(TAG, "selected song ${song.toString()}${idex}")
            musicViewModel.musicList.value?.let { musicService!!.setSongListToPlayer(it,idex) }


        }






        musicViewModel.musicList.observe(requireActivity(), Observer {

            Log.d(TAG, "musics ${it.toString()}")
            songsAdapter.differ.submitList(it)
            musicList= it as MutableList<MusicItem>
        })

        binding.apply {
            audioRv.layoutManager = GridLayoutManager(context, 3)
            audioRv.adapter = songsAdapter


        }

        val sharedPref= requireActivity().getSharedPreferences( FAVOURITE,Context.MODE_PRIVATE)
        val isFavCreated=sharedPref.getBoolean("FavCreated",false)

        if (!isFavCreated){
            createFavorite()
        }

//        binding.apply {
//
//
//            btFav.setOnClickListener { navigateToPlayListFragment() }
//            btPlaylist.setOnClickListener { navigateToPlayListFragment() }
//            btResent.setOnClickListener { navigateToPlayListFragment() }
//        }


    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.play_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId==R.id.play_list){
            navigateToPlayListFragment()
        }
        return super.onOptionsItemSelected(item)
    }


            private fun navigateToPlayListFragment() {
        findNavController().navigate(R.id.action_navigation_music_to_playListFragment)
    }

    private fun createFavorite(){
        musicViewModel.createPlayList(FAVOURITE)

        val     sharedPerf=requireActivity().getSharedPreferences( FAVOURITE,Context.MODE_PRIVATE)

       val editor=sharedPerf.edit()
        editor.putBoolean("FavCreated",true)
        editor.apply()
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