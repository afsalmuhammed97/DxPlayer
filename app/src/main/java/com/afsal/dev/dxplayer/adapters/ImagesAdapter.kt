package com.afsal.dev.dxplayer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afsal.dev.dxplayer.databinding.ImageItemBinding
import com.afsal.dev.dxplayer.interfacess.OnItemClickListner

class ImagesAdapter(private  val listener: OnItemClickListner): RecyclerView.Adapter<ImagesAdapter.ImagesHolder>() {



  inner  class ImagesHolder(val binding: ImageItemBinding):RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.image.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesHolder {
          return  ImagesHolder(ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {

 /// need to implement more like glide ...
    }

    override fun getItemCount()=8

}