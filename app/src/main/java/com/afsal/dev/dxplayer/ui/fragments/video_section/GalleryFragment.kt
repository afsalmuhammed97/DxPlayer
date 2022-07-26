package com.afsal.dev.dxplayer.ui.fragments.video_section

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.afsal.dev.dxplayer.adapters.VideosAdapter
import com.afsal.dev.dxplayer.databinding.FragmentVideoGalleryBinding
import com.afsal.dev.dxplayer.models.VideoSections.VideoItemModel
import com.afsal.dev.dxplayer.ui.fragments.BaseFragment
import com.afsal.dev.dxplayer.view_models.VidViewModel


class GalleryFragment : BaseFragment<FragmentVideoGalleryBinding>(
    FragmentVideoGalleryBinding::inflate
) {


    private val TAG="GalleryFragment"
    private var position:Int=0

    val args:GalleryFragmentArgs by navArgs()
    private lateinit var galleryViewModel: VidViewModel
    private lateinit var videosAdapter: VideosAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position=args.position
        Log.d(TAG,position.toString())
        galleryViewModel= ViewModelProvider(requireActivity()).get(VidViewModel::class.java)


        galleryViewModel.categoryVideoList.observe(viewLifecycleOwner, Observer { catogoryList->
            Log.d(TAG,catogoryList.toString())
            val videoList=catogoryList[position].videosList

            videosAdapter=VideosAdapter(videoList){ vid_position,video->



                galleryViewModel.launchPlayerScreen(requireContext(),video,this)
            }

            binding.gridVideoRv.adapter=videosAdapter
        })


        binding.gridVideoRv.apply {
            layoutManager= GridLayoutManager(context,3)
        }
    }


//    private   fun startPlayerActivity(video:VideoItemModel){
//       Log.d(TAG,"video data $video")
//       val intent= galleryViewModel.launchPlayerScreen(requireContext(),video)
//        startActivity(intent)
//
//
//            }



}