package com.afsal.dev.dxplayer.ui.fragments.music_section

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.adapters.PlayListAdapter
import com.afsal.dev.dxplayer.databinding.FragmentPlayListBinding
import com.afsal.dev.dxplayer.ui.fragments.BaseFragment
import com.afsal.dev.dxplayer.view_models.MusicViewModel
import com.google.android.material.textfield.TextInputEditText


class PlayListFragment() : BaseFragment<FragmentPlayListBinding>(
    FragmentPlayListBinding::inflate
) {
    private val TAG = "PlayListFragment"
    private lateinit var playListAdapter: PlayListAdapter
    private lateinit var musicViewModel: MusicViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicViewModel = ViewModelProvider(this)[MusicViewModel::class.java]

        playListAdapter = PlayListAdapter() {

            navigateToPlayListView(it.playListName)
        }

        setView()
        //  musicViewModel.createPlayList(name)


        binding.newBt.setOnClickListener {
            //  CoreUttiles.showSnackBar("clicked",binding.playListRv,""){}
            newPlayListDialog()
        }

        musicViewModel.playListNames.observe(viewLifecycleOwner, Observer {
            Log.d("PPP", "Playlist name  ${it.toString()}")

            //if (it.isNotEmpty())
            playListAdapter.differ.submitList(it)
        })

    }

    private fun setView() {
        binding.playListRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playListAdapter
        }
    }

    private fun navigateToPlayListView(playListName: String) {
        //   findNavController().navigate(R.id.action_playListFragment_to_playListViewFragment)

        val action =
            PlayListFragmentDirections.actionPlayListFragmentToPlayListViewFragment(playListName)
        findNavController().navigate(action)
    }

    private fun newPlayListDialog() {
        val alertDialog = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
        )
        val customAlert =
            LayoutInflater.from(context).inflate(R.layout.custom_dilog_layout, binding.root, false)

        val playListName = customAlert.findViewById<TextInputEditText>(R.id.play_list_text)

        alertDialog.setView(customAlert).also {

            it.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }

            it.setPositiveButton("create") { _: DialogInterface, i: Int ->

                if (!playListName.text.isNullOrEmpty())
                    Log.d(TAG, "text in dialog ${playListName.text.toString()}")
                musicViewModel.createPlayList(playListName.text.toString())
                //   CoreUttiles.showSnackBar(playListName.text.toString(),binding.playListRv ,""){}
            }

            it.create()
            it.show()

        }


    }
}