package com.afsal.dev.dxplayer.adaptersimport android.view.LayoutInflaterimport android.view.ViewGroupimport androidx.lifecycle.LiveDataimport androidx.recyclerview.widget.AsyncListDifferimport androidx.recyclerview.widget.DiffUtilimport androidx.recyclerview.widget.RecyclerViewimport androidx.viewpager.widget.PagerAdapterimport androidx.viewpager2.widget.ViewPager2import com.afsal.dev.dxplayer.databinding.PagerItemBindingimport com.afsal.dev.dxplayer.models.photosSections.ImageModelimport com.afsal.dev.dxplayer.utills.CorUttilesclass ImagePagerAdapter():RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {    private val diffCallback=object :DiffUtil.ItemCallback<ImageModel>(){        override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {            return oldItem.id==newItem.id        }        override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {          return oldItem==newItem        }    }    val differ=AsyncListDiffer(this,diffCallback)    class ImageViewHolder(val binding: PagerItemBinding):RecyclerView.ViewHolder(binding.root)    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {        return ImageViewHolder(PagerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))    }    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {    val photo=differ.currentList[position]       // val photo=list.value[position]        CorUttiles.loadImageIntoView(photo.contentUri,holder.binding.pagerImage,CorUttiles.IMAGE_VIEW_FRAGMENT)    }    override fun getItemCount()=differ.currentList.size}