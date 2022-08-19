package com.afsal.dev.dxplayer.adaptersimport android.net.Uriimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport androidx.recyclerview.widget.AsyncListDifferimport androidx.recyclerview.widget.DiffUtilimport androidx.recyclerview.widget.RecyclerViewimport com.afsal.dev.dxplayer.databinding.SongItemLayoutBindingimport com.afsal.dev.dxplayer.models.audioSections.MusicItemimport com.afsal.dev.dxplayer.utills.CoreUttilesimport com.afsal.dev.dxplayer.utills.CoreUttiles.SELECTION_SCREENclass SongSelectionAdapter(    val screenName:String, private val isSelected: (Music: MusicItem) -> Unit,    val optionClicked:(song:MusicItem)->Unit) :    RecyclerView.Adapter<SongSelectionAdapter.ItemHolder>() {    private val diffCallBack = object : DiffUtil.ItemCallback<MusicItem>() {        override fun areItemsTheSame(oldItem: MusicItem, newItem: MusicItem): Boolean {            return oldItem.id == newItem.id        }        override fun areContentsTheSame(oldItem: MusicItem, newItem: MusicItem): Boolean {            return oldItem == newItem        }    }    val differ = AsyncListDiffer(this, diffCallBack)    inner class ItemHolder(val binding: SongItemLayoutBinding) :        RecyclerView.ViewHolder(binding.root) {        fun bind(song: MusicItem) {            binding.apply {                titleText.text = song.tittle                 artistName.text=song.artist                CoreUttiles.loadImageIntoView(                    Uri.parse(song.imageUri),                    binding.songImage,                    null,                    CoreUttiles.IMAGE_VIEW                )                if (screenName==SELECTION_SCREEN){                    binding.checkBox.visibility=View.VISIBLE                    binding.moreBt.visibility=View.GONE                } else{                    binding.checkBox.visibility=View.GONE                    binding.moreBt.visibility=View.VISIBLE                    binding.rootLayout.setOnClickListener {                        isSelected(song)                    }                }               moreBt.setOnClickListener {                   optionClicked(song)               }                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->                    if (isChecked) {                        isSelected(song)                    }                }            }        }    }    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {        return ItemHolder(            SongItemLayoutBinding.inflate(                LayoutInflater.from(parent.context),                parent,                false            )        )    }    override fun onBindViewHolder(holder: ItemHolder, position: Int) {        val item = differ.currentList[position]        holder.bind(item)    }    override fun getItemCount() = differ.currentList.size    override fun onViewRecycled(holder: ItemHolder) {        holder.binding.checkBox.isChecked = false        super.onViewRecycled(holder)    }}