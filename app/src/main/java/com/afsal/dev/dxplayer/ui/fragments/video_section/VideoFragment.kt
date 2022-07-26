package com.afsal.dev.dxplayer.ui.fragments.video_section

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.dxplayer.adapters.BaseCategoryAdapter
import com.afsal.dev.dxplayer.adapters.RecentVideoAdapter
import com.afsal.dev.dxplayer.databinding.FragmentVideoBinding
import com.afsal.dev.dxplayer.interfacess.OnItemClickListner
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.ui.fragments.BaseFragment
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.view_models.VidViewModel

class VideoFragment :BaseFragment<FragmentVideoBinding>(
    FragmentVideoBinding::inflate
),OnItemClickListner {

    private lateinit var dataList:List<List<String>>

    private lateinit var recentVideoAdapter: RecentVideoAdapter
    private lateinit var categoryAdapter: BaseCategoryAdapter

private lateinit var videoViewModel:VidViewModel



    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        videoViewModel = ViewModelProvider(requireActivity()).get(VidViewModel::class.java)
        videoViewModel.loadVideosFromStorage()

                   videoViewModel.videoList.observe(viewLifecycleOwner, Observer {
                       Log.d("Videos", it.toString())

                       recentVideoAdapter.differ.submitList(it)
                       recentVideoAdapter.notifyDataSetChanged()

                   })


        videoViewModel.categoryVideoList.observe(viewLifecycleOwner, Observer {
            Log.d("Videos","Live data ${it.toString()}")
            categoryAdapter.differ.submitList(it)
            categoryAdapter.notifyDataSetChanged()
        })



    }


    private fun setRecyclerView(){
        recentVideoAdapter=RecentVideoAdapter()
        categoryAdapter=BaseCategoryAdapter({ position->
            //show more button

           val action=VideoFragmentDirections.actionNavigationVideoToGalleryFragment(position)

            findNavController().navigate(action)

        })
        { folderPositio,videoPosition,video->
           /* folder position and video postitions are need for playing from playlist

           not implemented yet
           * */

            videoViewModel.launchPlayerScreen(requireContext(),video,this)
        }

        binding.baseRv.apply {
            layoutManager= LinearLayoutManager(context)
            adapter= categoryAdapter

            binding.recentItemRv.apply {
                layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter=recentVideoAdapter
            }
        }


    }







    override fun onItemClick(Position: Int, photo: ImageModel) {

    }
}