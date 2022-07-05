package com.afsal.dev.dxplayer.ui.fragments.music_section

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afsal.dev.dxplayer.R

class MusicFragment : Fragment() {

    companion object {
        fun newInstance() = MusicFragment()
    }

    private lateinit var viewModel: MusicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.music_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MusicViewModel::class.java)
        // TODO: Use the ViewModel
    }

}