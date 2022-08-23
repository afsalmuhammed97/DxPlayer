package com.afsal.dev.dxplayer.adaptersimport android.net.Uriimport android.util.Logimport android.view.LayoutInflaterimport android.view.ViewGroupimport androidx.recyclerview.widget.AsyncListDifferimport androidx.recyclerview.widget.DiffUtilimport androidx.recyclerview.widget.RecyclerViewimport com.afsal.dev.dxplayer.Rimport com.afsal.dev.dxplayer.databinding.PlayListItemBindingimport com.afsal.dev.dxplayer.models.audioSections.PlayListsimport com.afsal.dev.dxplayer.utills.CoreUttilesimport com.afsal.dev.dxplayer.utills.CoreUttiles.FAVOURITEimport com.afsal.dev.dxplayer.utills.CoreUttiles.IMAGE_VIEW//  private val onLongClick:(playlist:String)->Unitclass PlayListAdapter(    private val onItemClick: (playList: PlayLists) -> Unit,    private val onLongLick: (playList: PlayLists) -> Unit) : RecyclerView.Adapter<PlayListAdapter.ItemHolder>() {    private val diffCallBack = object : DiffUtil.ItemCallback<PlayLists>() {        override fun areItemsTheSame(oldItem: PlayLists, newItem: PlayLists): Boolean {            return oldItem.playListName == newItem.playListName        }        override fun areContentsTheSame(oldItem: PlayLists, newItem: PlayLists): Boolean {            return oldItem == newItem        }    }    val differ = AsyncListDiffer(this, diffCallBack)    inner class ItemHolder(val binding: PlayListItemBinding) :        RecyclerView.ViewHolder(binding.root) {        init {            binding.rootLayout.setOnClickListener {                val position = absoluteAdapterPosition                onItemClick(differ.currentList[position])            }            binding.rootLayout.setOnLongClickListener {                val position = absoluteAdapterPosition                onLongLick(differ.currentList[position])                return@setOnLongClickListener true            }        }        fun bind(playList: PlayLists) {            binding.textView5.text = playList.playListName            binding.songCount.text = "${playList.songsCount} songs"            // binding.playlistImage.setImageResource()            if (playList.playListName == FAVOURITE) {                binding.playlistImage.setImageResource(R.drawable.ic_baseline_favorite_border_red)            } else {                if (playList.playListImage != null) {                    CoreUttiles.loadImageIntoView(                        Uri.parse(playList.playListImage),                        binding.playlistImage,                        null,                        IMAGE_VIEW                    )                }            }        }    }    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {        return ItemHolder(            PlayListItemBinding.inflate(                LayoutInflater.from(parent.context),                parent,                false            )        )    }    override fun onBindViewHolder(holder: ItemHolder, position: Int) {        val item = differ.currentList[position]        holder.bind(item)    }    override fun getItemCount() = differ.currentList.size}