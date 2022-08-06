package com.afsal.dev.dxplayer.ui.fragments.music_section

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afsal.dev.dxplayer.adapters.SongsAdapter
import com.afsal.dev.dxplayer.databinding.MusicFragmentBinding
import com.afsal.dev.dxplayer.ui.fragments.BaseFragment
import com.afsal.dev.dxplayer.view_models.MusicViewModel


class MusicFragment : BaseFragment<MusicFragmentBinding>(
    MusicFragmentBinding::inflate
) {

    private val TAG = "MusicFragment"
    private lateinit var musicViewModel: MusicViewModel
    private lateinit var songsAdapter: SongsAdapter
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.music_fragment, container, false)
//    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicViewModel = ViewModelProvider(this)[MusicViewModel::class.java]

        songsAdapter = SongsAdapter() {


        }
        binding.apply {
            audioRv.layoutManager = GridLayoutManager(context, 3)
            audioRv.adapter = songsAdapter


        }
        binding.audioRv.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            //            var scrollDy = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //  Log.d(TAG,"new state  $newState")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

//                if (dy >0){
//                    binding.headerLayout.visibility=View.VISIBLE
//                }else{
//                    binding.headerLayout.visibility=View.INVISIBLE
//                }
//                if (recyclerView.scrollState==RecyclerView.SCROLL_STATE_SETTLING){
//                    Log.d(TAG,"recyclerview setting dy $dy dx $dx" )
//                }else if (recyclerView.scrollState==RecyclerView.SCROLL_STATE_SETTLING){
//                    Log.d(TAG,"recyclerview scrolling dy $dy dx $dx\"")
//                }else if (recyclerView.scrollState==RecyclerView.SCROLL_STATE_IDLE){
//                    Log.d(TAG,"recyclerview idieal     dy $dy dx $dx\"")
//                }
//                else{
//
//                }
            }
        })



        musicViewModel.musicList.observe(viewLifecycleOwner, Observer {

            Log.d(TAG, "musics ${it.toString()}")
            songsAdapter.differ.submitList(it)
        })


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}