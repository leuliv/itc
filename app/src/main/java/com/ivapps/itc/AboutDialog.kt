package com.ivapps.itc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.ivapps.itc.R

class AboutDialog : DialogFragment() {

    val TAG = "AboutDialog"
    private var toolbar: Toolbar? = null
    private var v: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.dialog_about, container, false)

        toolbar = v!!.findViewById(R.id.about_toolbar)
        toolbar!!.setNavigationIcon(R.drawable.ic_close_black_24dp)
        toolbar!!.setNavigationOnClickListener {
            dialog!!.dismiss()
        }
        toolbar!!.title = "About"


        return v
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

}