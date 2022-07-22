package com.afsal.dev.dxplayer.ui.fragments.video_section

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.adapters.VideosAdapter
import com.afsal.dev.dxplayer.databinding.FragmentGalleryViewBinding
import com.afsal.dev.dxplayer.databinding.FragmentVideoGalleryBinding
import com.afsal.dev.dxplayer.ui.activities.PlayerScreenActivity
import com.afsal.dev.dxplayer.ui.fragments.Images_section.ImageViewFragmentArgs
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.view_models.VidViewModel


class GalleryFragment : Fragment() {
   private val TAG="GalleryFragment"
    private  var _binding: FragmentVideoGalleryBinding?=null
    private val galleryBinding get() = _binding!!

  private var position:Int=0

    val args:GalleryFragmentArgs by navArgs()
    private lateinit var galleryViewModel: VidViewModel
    private lateinit var videosAdapter: VideosAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        galleryViewModel= ViewModelProvider(requireActivity()).get(VidViewModel::class.java)
       _binding= FragmentVideoGalleryBinding.inflate(inflater,container,false)

        return galleryBinding.root
    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        if (item.itemId == androidx.appcompat.R.id.home)
//        Toast.makeText(context,"back pressed", Toast.LENGTH_SHORT).show()
//        return super.onOptionsItemSelected(item)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position=args.videoPosition
       Log.d(TAG,position.toString())

        galleryViewModel.categoryVideoList.observe(viewLifecycleOwner, Observer {
            Log.d(TAG,it.toString())
            videosAdapter=VideosAdapter(it[position].videosList){
                  // CoreUttiles.showSnackBar("position $it clicked",galleryBinding.gridVideoRv)

                startActivity()
            }
            galleryBinding.gridVideoRv.adapter=videosAdapter
        })


        galleryBinding.gridVideoRv.apply {
            layoutManager= GridLayoutManager(context,3)
        }
    }
    private   fun startActivity(){
                val intent=Intent(requireActivity(),PlayerScreenActivity::class.java)
                    startActivity(intent)
            }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}