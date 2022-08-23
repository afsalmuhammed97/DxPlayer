package com.afsal.dev.dxplayer.ui.fragments.music_section

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.adapters.SongSelectionAdapter
import com.afsal.dev.dxplayer.databinding.FragmentPlayListViewBinding
import com.afsal.dev.dxplayer.models.audioSections.MusicItem
import com.afsal.dev.dxplayer.ui.fragments.BaseFragment
import com.afsal.dev.dxplayer.ui.services.MusicService
import com.afsal.dev.dxplayer.view_models.MusicViewModel
import kotlin.concurrent.fixedRateTimer


class PlayListViewFragment : BaseFragment<FragmentPlayListViewBinding>(
    FragmentPlayListViewBinding::inflate
) {
    private val TAG = "PlayListViewFragment"
    private var musicService: MusicService? = null
    private lateinit var playListSongs: List<MusicItem>
    private lateinit var musicViewModel: MusicViewModel
    private lateinit var songSelectionAdapter: SongSelectionAdapter
    private val args: PlayListViewFragmentArgs by navArgs()


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MyBinder
            musicService = binder.currentService()


        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "MusicService connected $name")
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
        this.setHasOptionsMenu(true)
        musicViewModel = ViewModelProvider(requireActivity())[MusicViewModel::class.java]

        val playlistName = args.playListName

        (requireActivity() as AppCompatActivity).supportActionBar?.title = args.playListName.playListName


        songSelectionAdapter = SongSelectionAdapter("MusicFragment", { selected ->
            Log.d(TAG, "selected song ${selected.tittle}")
            //song item selected

            playSongFromPlayList(selected)

        }) {
            Log.d(TAG, "option song ${it.tittle}")
            //optionButton clicked from the song Item

            showDialogOption(it)
        }


        setView()
        musicViewModel.getSongsInPlayList(playlistName.playListName)

          binding.addButton.setOnClickListener {  navigateToSongsFragment() }

        musicViewModel.songsInPlayList.observe(requireActivity(), Observer {

            Log.d(TAG, "playlist songs ${it.toString()} ")

            songSelectionAdapter.differ.submitList(it)



            playListSongs = it
        })

     setAddButtonVisibility()


    }

    private fun setAddButtonVisibility(){

        if (args.playListName.songsCount ==0){
            binding.noContentLayout.visibility=View.VISIBLE
        }else{
            binding.noContentLayout.visibility=View.GONE
        }


    }
    private fun playSongFromPlayList(song: MusicItem) {


        val index = playListSongs.indexOf(song)

        musicService!!.setSongListToPlayer(playListSongs, index)


    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.option_menu, menu)
//
//        return super.onCreateOptionsMenu(menu)
//    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_item) {

         navigateToSongsFragment()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun navigateToSongsFragment(){
        val action =
            PlayListViewFragmentDirections.actionPlayListViewFragmentToSongsFragment(args.playListName.playListName)
        findNavController().navigate(action)
    }
    private fun setView() {
        binding.playListItemRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = songSelectionAdapter
        }
    }

    private fun showDialogOption(song: MusicItem) {

        //  OptionBottomSheet().show(childFragmentManager,"")
        val action = PlayListViewFragmentDirections.actionPlayListViewFragmentToOptionBottomSheet(
            song,
            args.playListName.playListName
        )
        findNavController().navigate(action)
    }
}