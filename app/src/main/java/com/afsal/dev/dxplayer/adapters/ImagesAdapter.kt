package com.afsal.dev.dxplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.afsal.dev.dxplayer.databinding.ImageItemBinding
import com.afsal.dev.dxplayer.interfacess.OnItemClickListner
import com.afsal.dev.dxplayer.models.photosSections.ImageModel
import com.afsal.dev.dxplayer.utills.CorUttiles

class ImagesAdapter(val context: Context, private  val listener: OnItemClickListner): RecyclerView.Adapter<ImagesAdapter.ImagesHolder>() {

          private val diffCallback= object :DiffUtil.ItemCallback<ImageModel>(){
        override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem.id== newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem==newItem
        }

    }
    val differList= AsyncListDiffer(this,diffCallback)

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
            val photo=differList.currentList[position]
       // holder.binding.image.setImageURI(photo.contentUri)

            CorUttiles.loadImageIntoView(photo.contentUri,holder.binding.image)

 /// need to implement more like glide ...
    }

    override fun getItemCount()=differList.currentList.size

}