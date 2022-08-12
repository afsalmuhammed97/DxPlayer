package com.afsal.dev.dxplayer.ui.fragments.Images_section

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.afsal.dev.dxplayer.adapters.ImagePagerAdapter
import com.afsal.dev.dxplayer.databinding.FragmentImageViewBinding


import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.ui.fragments.BaseFragment
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.view_models.PhotosViewModel


class ImageViewFragment : BaseFragment<FragmentImageViewBinding>(
    FragmentImageViewBinding::inflate
) {

    private lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val TAG = "ImageViewFragment"
    private lateinit var photoPager: ViewPager2
    private lateinit var imagePagerAdapter: ImagePagerAdapter
    private lateinit var photosViewModel: PhotosViewModel
    private lateinit var detector: GestureDetectorCompat
    private var isShown = false
    private var position: Int = 0
    private lateinit var currentImage: ImageModel
    val args: ImageViewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = args.position


        // detector = GestureDetectorCompat(requireContext(), eventListner())

        intentSenderLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {

                if (it.resultCode == RESULT_OK) {
                    CoreUttiles.showSnackBar(
                        "Photo deleted successfully",
                        binding.imageViewLayout,
                        ""
                    ) {}
                } else {
                    CoreUttiles.showSnackBar(
                        "Photo couldn't be deleted ",
                        binding.imageViewLayout,
                        ""
                    ) {}
                }


            }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photosViewModel = ViewModelProvider(requireActivity()).get(PhotosViewModel::class.java)
        initViewPager(position)
        photosViewModel.photoList.observe(requireActivity(), Observer {

//            imagePagerAdapter.differ.submitList(it)
            //  imagePagerAdapter.notifyDataSetChanged()
        })

        binding.apply {

            backBt.setOnClickListener {
                requireActivity().onBackPressed()
                // findNavController().navigate(R.id.action_imageViewFragment_to_navigation_images)
            }

            shareBt.setOnClickListener {
                CoreUttiles.shareImage(currentImage.contentUri, requireContext())
            }

            deleteBt.setOnClickListener {

                photosViewModel.deletePhoto(currentImage, requireContext(), intentSenderLauncher)

            }

            favoriteBt.setOnClickListener {
                CoreUttiles.showSnackBar(
                    "not implemented yet",
                    this.imageViewLayout,
                    ""
                ) {}
            }
            editBt.setOnClickListener {
                CoreUttiles.showSnackBar(
                    "not implemented yet",
                    this.imageViewLayout,
                    ""
                ) {}
            }

            moreBt.setOnClickListener {
                CoreUttiles.showSnackBar(
                    "not implemented yet",
                    this.imageViewLayout,
                    ""
                ) {}
            }

        }


//
//            imageViewBinding.apply {
//                bottomLayout.visibility=View.GONE
//                toolbarLayout.visibility=View.GONE
//            }


    }

    private fun initViewPager(position: Int) {

        photoPager = binding.imagePager
        imagePagerAdapter = ImagePagerAdapter(position) { it ->
            binding.dateText.text = it.addedDate
            Log.d("ITEM", "current item $it.toString()")
            currentImage = it
        }

        photosViewModel.photoList.observe(requireActivity(), Observer {

            imagePagerAdapter.differ.submitList(it)
            imagePagerAdapter.notifyDataSetChanged()
        })

        Log.d(TAG, "photos  ${photosViewModel.photoList.value.toString()}")
        photoPager.adapter = imagePagerAdapter
        photoPager.setCurrentItem(position, false)


    }


    inner class eventListner : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.d(TAG, "onTap event   $e")
            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.d(TAG, "double tap event $e")
            return super.onDoubleTap(e)
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            Log.d(TAG, "onsingle Tap confirmed   $e")
            return super.onSingleTapConfirmed(e)
        }
    }


}