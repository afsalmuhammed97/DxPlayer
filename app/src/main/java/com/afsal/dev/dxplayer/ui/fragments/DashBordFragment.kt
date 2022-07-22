package com.afsal.dev.dxplayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.afsal.dev.dxplayer.R
import com.afsal.dev.dxplayer.databinding.FragmentDashBordBinding


class DashBordFragment : Fragment() {

    private    var _binding: FragmentDashBordBinding ?= null
    private val dashboardBinding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding= FragmentDashBordBinding.inflate(inflater,container,false)

        return dashboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



     //   val navController= findNavController(requireActivity(),R.id.nav_host_fragment) ///(R.id.nav_host_fragment_activity_dash_bord)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_video,
                R.id.navigation_files, R.id.navigation_images)
        )
       // setupActionBarWithNavController(navController, appBarConfiguration)
    //    navView.setupWithNavController(navController)

    }



}