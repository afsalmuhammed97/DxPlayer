package com.afsal.dev.dxplayer.ui.fragmentsimport android.app.Dialogimport android.os.Bundleimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport android.view.WindowManagerimport com.afsal.dev.dxplayer.Rimport com.google.android.material.bottomsheet.BottomSheetBehaviorimport com.google.android.material.bottomsheet.BottomSheetDialogimport com.google.android.material.bottomsheet.BottomSheetDialogFragmentclass DialogBottomSheet:BottomSheetDialogFragment() {    override fun onCreateView(        inflater: LayoutInflater,        container: ViewGroup?,        savedInstanceState: Bundle?    ): View =   inflater.inflate(R.layout.bottom_sheet_dialog,container,false)    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {        val dialog = BottomSheetDialog(requireContext(), theme)        dialog.setOnShowListener {            val bottomSheetDialog = it as BottomSheetDialog            val parentLayout =                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)            parentLayout?.let { it ->                val behaviour = BottomSheetBehavior.from(it)                setupFullHeight(it)                behaviour.state = BottomSheetBehavior.STATE_EXPANDED            }        }        return dialog      //  return super.onCreateDialog(savedInstanceState)    }    override fun onActivityCreated(savedInstanceState: Bundle?) {        super.onActivityCreated(savedInstanceState)           //initialize the things inside the layout    }    private fun setupFullHeight(bottomSheet: View) {        val layoutParams = bottomSheet.layoutParams        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT        bottomSheet.layoutParams = layoutParams    }}