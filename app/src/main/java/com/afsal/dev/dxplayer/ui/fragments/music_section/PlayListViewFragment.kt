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


class PlayListViewFragment : BaseFragment<FragmentPlayListViewBinding>(
    FragmentPlayListViewBinding::inflate
) {
    private val TAG = "PlayListViewFragment"
    private var musicService: MusicService? = null
    private lateinit var musicViewModel: MusicViewModel
    private lateinit var songSelectionAdapter: SongSelectionAdapter
    val args: PlayListViewFragmentArgs by navArgs()


    private val connection=object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder=service as MusicService.MyBinder
            musicService=binder.currentService()

            musicViewModel.songsInPlayList.observe(viewLifecycleOwner, Observer {
                musicService!!.songsList.value = it
            })
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



        songSelectionAdapter = SongSelectionAdapter("MusicFragment", { selected ->
            Log.d(TAG, "selected song ${selected.tittle}")//song item clicked

            musicService!!.setMediaItem(selected)

        }) {
            Log.d(TAG, "option song ${it.tittle}")//optionButton clicked
            showDialogOption(it)
        }
        setView()
        musicViewModel.getSongsInPlayList(playlistName)



        musicViewModel.songsInPlayList.observe(requireActivity(), Observer {

            Log.d(TAG, "playlist songs ${it.toString()} ")
            songSelectionAdapter.differ.submitList(it)
        })
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_item) {

            val action =
                PlayListViewFragmentDirections.actionPlayListViewFragmentToSongsFragment(args.playListName)
            findNavController().navigate(action)
        }
        return super.onOptionsItemSelected(item)
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
            args.playListName
        )
        findNavController().navigate(action)
    }
}