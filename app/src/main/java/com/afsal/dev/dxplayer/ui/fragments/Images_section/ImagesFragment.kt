package com.afsal.dev.dxplayer.ui.fragments.Images_section

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.adapters.ImagesAdapter
import com.afsal.dev.dxplayer.adapters.ImagesBaseAdapter
import com.afsal.dev.dxplayer.databinding.FragmentImagesBinding
import com.afsal.dev.dxplayer.interfacess.OnItemClickListner
import com.afsal.dev.dxplayer.view_models.PhotosViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


class ImagesFragment : Fragment(),OnItemClickListner {

    private lateinit var photosViewModel: PhotosViewModel
   private lateinit var imagesAdapter: ImagesAdapter
    private var _binding:FragmentImagesBinding ? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       photosViewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)

        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setGridRv()
//         setBaseRv()
     //   val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photosViewModel.loadSystemImages(requireContext())
        photosViewModel.photoList.observe(requireActivity(), Observer {
               imagesAdapter.differList.submitList(it)
               imagesAdapter.notifyDataSetChanged()
        })
    }

     private fun setGridRv(){
         imagesAdapter= ImagesAdapter(requireContext(),this)

             binding.imagesBaseRv.apply {
             layoutManager= GridLayoutManager(context,4)
             adapter=imagesAdapter
         }

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
       // findNavController().navigate(R.id.action_navigation_images_to_galleryViewFragment)
    }
}