package com.afsal.dev.dxplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afsal.dev.dxplayer.databinding.ImageCardBaseItemBinding
import com.afsal.dev.dxplayer.interfacess.OnItemClickListner

class ImagesBaseAdapter(context: Context,private  val listener: OnItemClickListner):RecyclerView.Adapter<ImagesBaseAdapter.BaseItemHolder>() {

    val timeList= listOf("6 Jan","4 Mar" ,"23 May","17 Jul" )
    class BaseItemHolder(val binding: ImageCardBaseItemBinding):RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagesBaseAdapter.BaseItemHolder {
      return BaseItemHolder(ImageCardBaseItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ImagesBaseAdapter.BaseItemHolder, position: Int) {
          holder.binding.categoryDateTx.text=timeList[position]
         val imagesAdapter =ImagesAdapter(listener)

         holder.binding.imageItemRv.apply {
             layoutManager=GridLayoutManager(context,4)
                adapter=imagesAdapter
         }



    }

    override fun getItemCount()=timeList.size

}