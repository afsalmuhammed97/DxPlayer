package com.afsal.dev.dxplayer.ui.fragments.music_section

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.dxplayer.adapters.SongSelectionAdapter
import com.afsal.dev.dxplayer.databinding.FragmentSongsBinding
import com.afsal.dev.dxplayer.ui.fragments.BaseFragment
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.view_models.MusicViewModel

class SongsFragment : BaseFragment<FragmentSongsBinding>(
    FragmentSongsBinding::inflate
) {

    private lateinit var musicViewModel: MusicViewModel
    private lateinit var songSelectionAdapter: SongSelectionAdapter
    val args: SongsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        musicViewModel = ViewModelProvider(requireActivity())[MusicViewModel::class.java]

        songSelectionAdapter = SongSelectionAdapter(CoreUttiles.SELECTION_SCREEN,{

            musicViewModel.addSongsToPlaylist(it, args.playListName)
        }) {

            Log.d("PPPP", "selected song ${it.tittle} playlistname ${args.playListName}")

        }
        setView()


    }

    private fun setView() {

        binding.allSongsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = songSelectionAdapter
        }

        musicViewModel.musicList.observe(requireActivity(), Observer {

            songSelectionAdapter.differ.submitList(it)
            Log.d("PPPP", "songsList${it.toString()}")

        })

    }
}