package com.afsal.dev.dxplayer.ui.fragments.music_sectionimport android.os.Bundleimport android.util.Logimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport androidx.lifecycle.ViewModelProviderimport androidx.navigation.fragment.navArgsimport com.afsal.dev.dxplayer.Rimport com.afsal.dev.dxplayer.databinding.OptionBottomSheetLayoutBindingimport com.afsal.dev.dxplayer.models.audioSections.MusicItemimport com.afsal.dev.dxplayer.utills.CoreUttilesimport com.afsal.dev.dxplayer.view_models.MusicViewModelimport com.google.android.material.bottomsheet.BottomSheetDialogFragmentclass OptionBottomSheet : BottomSheetDialogFragment() {    private lateinit var musicViewModel: MusicViewModel    private lateinit var binding: OptionBottomSheetLayoutBinding    val args: OptionBottomSheetArgs by navArgs()    override fun onCreateView(        inflater: LayoutInflater,        container: ViewGroup?,        savedInstanceState: Bundle?    ): View? {        binding = OptionBottomSheetLayoutBinding.inflate(inflater, container, false)        Log.d("DDDD", "song data${args.songData.tittle}")        return binding.root    }    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        super.onViewCreated(view, savedInstanceState)        val song = args.songData        musicViewModel = ViewModelProvider(requireActivity())[MusicViewModel::class.java]        binding.apply {            tittleText.text = song.tittle            artistName.text = song.artist            folderName.text = song.folderName        }        binding.deleteBt.setOnClickListener {            //showDeleteDialog(song, args.playListName)            val text = "Remove from playList"            CoreUttiles.showDeleteDialog(text, requireContext(), this) {                deleteSongFromList(args.playListName, song)            }            dialog!!.cancel()        }    }    private fun deleteSongFromList(playList: String, song: MusicItem) {        if (playList.isNotEmpty()) {            musicViewModel.deleteSongFromPlaylist(song, playList)        }    }    //  setStyle(STYLE_NORMAL, R.style. AppBottomSheetDialogTheme)//    private fun showDeleteDialog(song: MusicItem, playList: String) {//        Log.d("BBB", "PlaylistName $playList")////        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()////        val customView = layoutInflater.inflate(R.layout.custom_alert_layout, null)//        val deleteBt = customView.findViewById<Button>(R.id.remove_bt)//        val cancelBt = customView.findViewById<Button>(R.id.cancel_bt)//        deleteBt.text = "Remove from playList"                //@string/remove_from_playlist//        dialog.setView(customView)////        deleteBt.setOnClickListener {////            if (playList.isNotEmpty()) {//                musicViewModel.deleteSongFromPlaylist(song, playList)//            }//            dialog.dismiss()//        }//        cancelBt.setOnClickListener {//            dialog.dismiss()//        }////////        dialog.show()//    }    override fun getTheme(): Int {        return R.style.AppBottomSheetDialogTheme    }}