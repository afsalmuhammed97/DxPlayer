package com.afsal.dev.dxplayer.ui.fragments.music_section

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.adapters.PlayListAdapter
import com.afsal.dev.dxplayer.databinding.FragmentPlayListBinding
import com.afsal.dev.dxplayer.ui.fragments.BaseFragment
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.view_models.MusicViewModel
import com.google.android.material.textfield.TextInputEditText


class PlayListFragment ():BaseFragment<FragmentPlayListBinding>(
    FragmentPlayListBinding::inflate
) {
    private lateinit var playListAdapter: PlayListAdapter
    private lateinit var musicViewModel: MusicViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicViewModel = ViewModelProvider(this)[MusicViewModel::class.java]

          playListAdapter= PlayListAdapter()

           setView()
              //  musicViewModel.createPlayList(name)


           binding.newBt.setOnClickListener {
             //  CoreUttiles.showSnackBar("clicked",binding.playListRv,""){}
               newPlayListDialog()
           }

//        musicViewModel.allPlayList.observe(viewLifecycleOwner, Observer {
//            Log.d("PPP","Playlist name  ${it.toString()}")
//        })

    }

    private fun setView(){
        binding.playListRv.apply {
          layoutManager=LinearLayoutManager(context)
            adapter=playListAdapter
        }
    }

    private fun newPlayListDialog(){
        val alertDialog=AlertDialog.Builder(requireContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
        val customAlert=LayoutInflater.from(context).inflate(R.layout.custom_dilog_layout,binding.root,false)
        val playListName = customAlert.findViewById<TextInputEditText>(R.id.play_list_text)

        alertDialog.setView(customAlert).also {

            it.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }

            it.setPositiveButton("create") { _: DialogInterface, i: Int ->

                if (playListName.text.isNullOrEmpty())
                CoreUttiles.showSnackBar(playListName.text.toString(),binding.playListRv,""){}
            }

            it.create()
            it.show()

        }




    }
}