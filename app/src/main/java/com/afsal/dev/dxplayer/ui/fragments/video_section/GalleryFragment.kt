package com.afsal.dev.dxplayer.ui.fragments.video_section

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afsal.dev.dxplayer.R


class GalleryFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_gallery, container, false)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == androidx.appcompat.R.id.home)
        Toast.makeText(context,"back pressed", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }



}