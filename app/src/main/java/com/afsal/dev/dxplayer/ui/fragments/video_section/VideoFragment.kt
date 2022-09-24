package com.afsal.dev.dxplayer.ui.fragments.video_section

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.adapters.BaseCategoryAdapter
import com.afsal.dev.dxplayer.adapters.RecentVideoAdapter
import com.afsal.dev.dxplayer.databinding.FragmentVideoBinding
import com.afsal.dev.dxplayer.interfacess.OnItemClickListner
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.ui.fragments.BaseFragment
import com.afsal.dev.dxplayer.utills.CoreUttiles
import com.afsal.dev.dxplayer.view_models.VidViewModel

class VideoFragment : BaseFragment<FragmentVideoBinding>(
    FragmentVideoBinding::inflate
), OnItemClickListner {

    private lateinit var dataList: List<List<String>>

    private lateinit var recentVideoAdapter: RecentVideoAdapter
    private lateinit var categoryAdapter: BaseCategoryAdapter

    private lateinit var videoViewModel: VidViewModel


    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("NotifyDataSetChanged")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setHasOptionsMenu(true)
        videoViewModel = ViewModelProvider(requireActivity())[VidViewModel::class.java]
        videoViewModel.loadVideosFromStorage()

        setRecyclerView()


//        videoViewModel.videoList.observe(requireActivity(), Observer {
//            Log.d("VVV", "all local videos ${it.toString()}")
//        })


        videoViewModel.categoryVideoList.observe(viewLifecycleOwner, Observer {
            Log.d("VVV", "Live data ${it.toString()}")
            categoryAdapter.differ.submitList(it)
            // categoryAdapter.notifyDataSetChanged()
        })


        videoViewModel.watchedHistory.observe(requireActivity(), Observer {

            if (it.isEmpty()) {
                binding.recntWatchedText.visibility = View.GONE
            } else {
                binding.recntWatchedText.visibility = View.VISIBLE
            }

            recentVideoAdapter.differ.submitList(it)

            Log.d("TTT", "videoHistory ${it.toString()}")
            Log.d("JJJ", "videoHistorySize  ${it.size}")
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

          inflater.inflate(R.menu.video_screen_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

           if (item.itemId==R.id.clare){
               clearWatchHistory()
           }
        return super.onOptionsItemSelected(item)
    }
    private fun setRecyclerView() {
        recentVideoAdapter = RecentVideoAdapter { lastPlayedId ->

            setPlayerScreenWithRecentItem(lastPlayedId)
        }

        categoryAdapter = BaseCategoryAdapter({ position ->
            //show more button

            val action = VideoFragmentDirections.actionNavigationVideoToGalleryFragment(position)

            findNavController().navigate(action)

        })
        { folderPositio, videoPosition, video ->
            /* folder position and video postitions are need for playing from playlist

            not implemented yet
            * */

            videoViewModel.launchPlayerScreen(requireContext(), video, this)
        }

        binding.baseRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter

            binding.recentItemRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = recentVideoAdapter
            }
        }


    }


    override fun onResume() {
        super.onResume()

        Log.i("TTT", "onResume called")

        val lastPlayed = CoreUttiles.retrievingPlayedVideoId(requireContext())

        if (lastPlayed!!.videoId != 0L || lastPlayed.videoUri.isNotEmpty()) {


            videoViewModel.addVideoIntoHistory(lastPlayed) //add to historyDb


        }

        //  videoViewModel.loadWatchHistory()
        Log.d("TTT", "last played video ${lastPlayed.toString()}")


    }


    private fun setPlayerScreenWithRecentItem(lastPlayedId: Long) {

        for (item in videoViewModel.videoList.value!!) {

            if (item.id == lastPlayedId) {
                videoViewModel.launchPlayerScreen(requireContext(), item, this)

                break
            }
        }
    }

    override fun onItemClick(Position: Int, photo: ImageModel) {

    }

    private fun clearWatchHistory(){
            videoViewModel.clearWatchHistory()
    }

}