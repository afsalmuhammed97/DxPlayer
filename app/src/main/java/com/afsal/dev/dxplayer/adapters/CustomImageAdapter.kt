package com.afsal.dev.dxplayer.adaptersimport android.view.Viewimport android.view.ViewGroupimport android.widget.ImageViewimport android.widget.TextViewimport androidx.recyclerview.widget.RecyclerViewimport com.afsal.dev.dxplayer.Rclass CustomImageAdapter():RecyclerView.Adapter<CustomImageAdapter.ViewHolder>() {    var DATE_TEXT=true    class ViewHolder( val containerView:View):RecyclerView.ViewHolder(containerView) {    }    override fun onCreateViewHolder(        parent: ViewGroup,        viewType: Int    ): CustomImageAdapter.ViewHolder {        TODO("Not yet implemented")    }    override fun onBindViewHolder(holder: CustomImageAdapter.ViewHolder, position: Int) {        TODO("Not yet implemented")    }    override fun getItemCount(): Int {        TODO("Not yet implemented")    }}