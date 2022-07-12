package com.afsal.dev.dxplayer.ui.fragments.video_section

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.adapters.BaseCategoryAdapter
import com.afsal.dev.dxplayer.adapters.RecentVideoAdapter
import com.afsal.dev.dxplayer.adapters.VideosAdapter
import com.afsal.dev.dxplayer.databinding.FragmentVideoBinding
import com.afsal.dev.dxplayer.interfacess.OnItemClickListner
import com.afsal.dev.dxplayer.view_models.VidViewModel

class VideoFragment : Fragment(),OnItemClickListner {

    private lateinit var dataList:List<List<String>>

    private lateinit var recentVideoAdapter: RecentVideoAdapter
    private lateinit var categoryAdapter: BaseCategoryAdapter
    private var _binding:FragmentVideoBinding? = null

    private val binding get() = _binding!!
private lateinit var homeViewModel:VidViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         homeViewModel = ViewModelProvider(this).get(VidViewModel::class.java)

        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        val root: View = binding.root
          createDataList()
        val vodList= listOf("43","33","24","65","37","76")



           setRecyclerView()

        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recentVideoAdapter=RecentVideoAdapter()
         homeViewModel.photoList.observe(viewLifecycleOwner, Observer {

             recentVideoAdapter.differ.submitList(it)
             recentVideoAdapter.notifyDataSetChanged()

         })

        binding.recentItemRv.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=recentVideoAdapter
        }
    }


    private fun setRecyclerView(){

        binding.baseRv.apply {
            layoutManager= LinearLayoutManager(context)
            adapter=categoryAdapter
        }


    }




    fun createDataList(){

        val vodList1= listOf("43","33","24","65","37","76","90","45","57","65")
        val vodList2= listOf("43","33","24","65","37","76","90","45","57","65")
        val vodList3= listOf("43","33","24","65","37","76","90","45","57","65")
        val vodList4= listOf("43","33","24","65","37","76","90","45","57","65")
        val vodList5= listOf("43","33","24","65","37","76","90","45","57","65")
        val vodList6= listOf("43","33","24","65","37","76","90","45","57","65")

        dataList = listOf(vodList1,vodList2,vodList3,vodList4,vodList6,vodList5)
        categoryAdapter=BaseCategoryAdapter(dataList,this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(Position: Int) {
       findNavController().navigate(R.id.action_navigation_video_to_galleryFragment)
    }
}