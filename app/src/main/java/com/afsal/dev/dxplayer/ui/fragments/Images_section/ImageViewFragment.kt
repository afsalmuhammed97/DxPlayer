package com.afsal.dev.dxplayer.ui.fragments.Images_section

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.afsal.dev.dxplayer.adapters.ImagePagerAdapter
import com.afsal.dev.dxplayer.databinding.FragmentImageViewBinding
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.view_models.PhotosViewModel


class ImageViewFragment : Fragment() {

    private lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val TAG="ImageViewFragment"
    private lateinit var photoPager: ViewPager2
    private lateinit var imagePagerAdapter: ImagePagerAdapter
    private lateinit var  photosViewModel:PhotosViewModel
    private  var _imageViewBinding: FragmentImageViewBinding? =null
    private val imageViewBinding get() = _imageViewBinding!!


    private var position:Int=0
    private lateinit var currentImage:ImageModel
    val args:ImageViewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        intentSenderLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {

                if (it.resultCode == RESULT_OK) {
                   CoreUttiles.showSnackBar("Photo deleted successfully",imageViewBinding.imageViewLayout)
                }else{
                    CoreUttiles.showSnackBar("Photo couldn't be deleted ",imageViewBinding.imageViewLayout)
                }


            }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        position=args.position
       // currentImage=args.photoData

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

        photosViewModel.photoList.observe(requireActivity(), Observer {

//            imagePagerAdapter.differ.submitList(it)
          //  imagePagerAdapter.notifyDataSetChanged()
        })

             imageViewBinding.apply {

                 backBt.setOnClickListener{
                     requireActivity().onBackPressed()
                     // findNavController().navigate(R.id.action_imageViewFragment_to_navigation_images)
                 }

                 shareBt.setOnClickListener {
                                                  CoreUttiles.shareImage(currentImage.contentUri,requireContext()) }

                 deleteBt.setOnClickListener {

                     photosViewModel.deletePhoto(currentImage,requireContext(),intentSenderLauncher)

                 }

                 favoriteBt.setOnClickListener { CoreUttiles.showSnackBar("not implemented yet",this.imageViewLayout) }
                 editBt.setOnClickListener { CoreUttiles.showSnackBar("not implemented yet",this.imageViewLayout) }

                 moreBt.setOnClickListener { CoreUttiles.showSnackBar("not implemented yet",this.imageViewLayout) }

             }


//
//            imageViewBinding.apply {
//                bottomLayout.visibility=View.GONE
//                toolbarLayout.visibility=View.GONE
//            }




    }
    private fun initViewPager(position:Int) {

        photoPager=imageViewBinding.imagePager
        imagePagerAdapter= ImagePagerAdapter(position){it ->
            imageViewBinding.dateText.text=it.addedDate
            Log.d("ITEM", "current item $it.toString()")
            currentImage=it
        }

        photosViewModel.photoList.observe(requireActivity(), Observer {

            imagePagerAdapter.differ.submitList(it)
              imagePagerAdapter.notifyDataSetChanged()
        })

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