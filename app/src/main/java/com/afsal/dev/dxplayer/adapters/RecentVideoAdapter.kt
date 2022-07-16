package com.afsal.dev.dxplayer.adaptersimport android.view.LayoutInflaterimport android.view.ViewGroupimport androidx.recyclerview.widget.AsyncListDifferimport androidx.recyclerview.widget.DiffUtilimport androidx.recyclerview.widget.RecyclerViewimport com.afsal.dev.dxplayer.databinding.RecentVideoItemBindingimport com.afsal.dev.dxplayer.models.photosSections.ImageModelclass RecentVideoAdapter():RecyclerView.Adapter<RecentVideoAdapter.RecentVodItemHolder>() {    private val diffCallback=object :DiffUtil.ItemCallback<ImageModel>(){        override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {            return oldItem.id==newItem.id        }        override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {          return oldItem==newItem        }    }     val differ=AsyncListDiffer(this,diffCallback)    class RecentVodItemHolder(val binding: RecentVideoItemBinding):RecyclerView.ViewHolder(binding.root) {}    override fun onCreateViewHolder(        parent: ViewGroup,        viewType: Int    ): RecentVideoAdapter.RecentVodItemHolder {       return  RecentVodItemHolder(RecentVideoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))    }    override fun onBindViewHolder(holder: RecentVideoAdapter.RecentVodItemHolder, position: Int) {        val photo=differ.currentList[position]        holder.binding.recentVideoImage.setImageURI(photo.contentUri)    }    override fun getItemCount()=differ.currentList.size}