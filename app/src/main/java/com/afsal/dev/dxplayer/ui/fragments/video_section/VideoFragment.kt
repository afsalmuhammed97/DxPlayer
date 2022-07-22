package com.afsal.dev.dxplayer.ui.fragments.video_section

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.adapters.BaseCategoryAdapter
import com.afsal.dev.dxplayer.adapters.RecentVideoAdapter
import com.afsal.dev.dxplayer.databinding.FragmentVideoBinding
import com.afsal.dev.dxplayer.databinding.ImageItemBinding
import com.afsal.dev.dxplayer.interfacess.OnItemClickListner
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.view_models.VidViewModel

class VideoFragment : Fragment(),OnItemClickListner {

    private lateinit var dataList:List<List<String>>

    private lateinit var recentVideoAdapter: RecentVideoAdapter
    private lateinit var categoryAdapter: BaseCategoryAdapter
    private var _binding:FragmentVideoBinding? = null

    private val binding get() = _binding!!
private lateinit var videoViewModel:VidViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         videoViewModel = ViewModelProvider(requireActivity()).get(VidViewModel::class.java)

        _binding = FragmentVideoBinding.inflate(inflater, container, false)

        val root: View = binding.root

        setRecyclerView()

        return root
    }


    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
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
           // CoreUttiles.showSnackBar("item Position $position",binding.root)
           val action=VideoFragmentDirections.actionNavigationVideoToGalleryFragment(position)
            findNavController().navigate(action)
        })
        { videoPosition->

            CoreUttiles.showSnackBar("item cliked at $videoPosition",binding.root)
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






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(Position: Int, photo: ImageModel) {

    }
}