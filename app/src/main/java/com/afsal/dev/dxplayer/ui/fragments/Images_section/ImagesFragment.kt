package com.afsal.dev.dxplayer.ui.fragments.Images_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.adapters.ImagesBaseAdapter
import com.afsal.dev.dxplayer.databinding.FragmentImagesBinding
import com.afsal.dev.dxplayer.interfacess.OnItemClickListner

class ImagesFragment : Fragment(),OnItemClickListner {

    private var _binding:FragmentImagesBinding ? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(PhotosViewModel::class.java)

        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        val root: View = binding.root

         setBaseRv()
//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setBaseRv(){
        val imagesBaseAdapter=ImagesBaseAdapter(requireContext(),this)
        binding.imagesBaseRv.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=imagesBaseAdapter
        }
    }
// callback from imageList
    override fun onItemClick(Position: Int) {
       findNavController().navigate(R.id.action_navigation_images_to_imageViewFragment)
    }
}