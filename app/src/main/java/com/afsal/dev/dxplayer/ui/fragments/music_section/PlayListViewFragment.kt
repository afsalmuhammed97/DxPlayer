package com.afsal.dev.dxplayer.ui.fragments.music_section

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.adapters.SongSelectionAdapter
import com.afsal.dev.dxplayer.databinding.FragmentPlayListViewBinding
import com.afsal.dev.dxplayer.ui.fragments.BaseFragment
import com.afsal.dev.dxplayer.view_models.MusicViewModel


class PlayListViewFragment :BaseFragment<FragmentPlayListViewBinding>(
    FragmentPlayListViewBinding::inflate
) {
    private val TAG="PlayListViewFragment"
    private lateinit var musicViewModel:MusicViewModel
    private lateinit var songSelectionAdapter: SongSelectionAdapter
    val args:PlayListViewFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setHasOptionsMenu(true)
        musicViewModel = ViewModelProvider(this)[MusicViewModel::class.java]

        val playlistName=args.playListName

        songSelectionAdapter= SongSelectionAdapter("MusicFragment",{selected->
            Log.d(TAG,"selected song ${selected.tittle}")//song item clicked



        }) {
            Log.d(TAG,"option song ${it.tittle}")//optionButton clicked
                    showDialogOption()
        }
        setView()
        musicViewModel.getSongsInPlayList(playlistName)



        musicViewModel.songsInPlayList.observe(requireActivity(), Observer {

            Log.d(TAG,"playlist songs ${it.toString()} ")
            songSelectionAdapter.differ.submitList(it)
        })
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.option_menu, menu)
//
//        return super.onCreateOptionsMenu(menu)
//    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.option_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId==R.id.add_item){

            val action=PlayListViewFragmentDirections.actionPlayListViewFragmentToSongsFragment(args.playListName)
            findNavController().navigate(action)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setView(){
        binding.playListItemRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = songSelectionAdapter
        }
    }
private fun showDialogOption(){
  //  DialogBottomSheet().show(supportFragmentManager, "")
    findNavController().navigate(R.id.action_playListViewFragment_to_optionBottomSheet)
}
}