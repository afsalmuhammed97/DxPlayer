package com.afsal.dev.dxplayer.ui.fragments.Images_section

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.afsal.dev.dxplayer.R


class ImageViewFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_view, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == androidx.appcompat.R.id.home)
//        Toast.makeText(context,"back pressed", Toast.LENGTH_SHORT).show()
//        return super.onOptionsItemSelected(item)
//    }






}