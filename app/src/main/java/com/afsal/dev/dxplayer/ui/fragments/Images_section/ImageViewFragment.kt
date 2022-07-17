package com.afsal.dev.dxplayer.ui.fragments.Images_section

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.afsal.dev.dxplayer.adapters.ImagePagerAdapter
import com.afsal.dev.dxplayer.databinding.FragmentImageViewBinding
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.view_models.PhotosViewModel


class ImageViewFragment : Fragment() {
private val TAG="ImageViewFragment"
private lateinit var photoPager: ViewPager2
private lateinit var imagePagerAdapter: ImagePagerAdapter
private lateinit var  photosViewModel:PhotosViewModel
private  var _imageViewBinding: FragmentImageViewBinding? =null
    private val imageViewBinding get() = _imageViewBinding!!

    private var position:Int=0
    private lateinit var currentImage:ImageModel
    val args:ImageViewFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        position=args.position
        currentImage=args.photoData

//        (activity as AppCompatActivity?)!!.apply {
//            supportActionBar!!.hide()
//
//
//        }


       photosViewModel  = ViewModelProvider(requireActivity()).get(PhotosViewModel::class.java)
        _imageViewBinding = FragmentImageViewBinding.inflate(inflater, container, false)
        initViewPager(position)
        return imageViewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        photosViewModel.photoList.observe(requireActivity(), Observer {
//                 imagePagerAdapter.differ.submitList(it)
//                 imagePagerAdapter.notifyDataSetChanged()
//            Log.d(TAG,"imageList ${it}")
//
//        })

    }
    private fun initViewPager(position:Int) {

        photoPager=imageViewBinding.imagePager
        imagePagerAdapter= ImagePagerAdapter()
        imagePagerAdapter.differ.submitList(photosViewModel.photoList.value)

         Log.d(TAG,"photos  ${photosViewModel.photoList.value.toString()}")
        photoPager.adapter=imagePagerAdapter
        photoPager.setCurrentItem(position,false)



    }







//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == androidx.appcompat.R.id.home)
//        Toast.makeText(context,"back pressed", Toast.LENGTH_SHORT).show()
//        return super.onOptionsItemSelected(item)
//    }

    override fun onDestroy() {
        super.onDestroy()
        _imageViewBinding=null
    }




}