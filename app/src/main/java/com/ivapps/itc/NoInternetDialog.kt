package com.ivapps.itc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class NoInternetDialog(val detail:String) : DialogFragment() {

    val TAG = "NoInternetDialog"
    private var v: View? = null
    private var act:Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        act = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.dialog_no_internet, container, false)

        val retryBtn = v!!.findViewById<Button>(R.id.retry_btn)
        val changeBtn = v!!.findViewById<Button>(R.id.change_settings_btn)
        v!!.findViewById<TextView>(R.id.error_detail).text = detail
        retryBtn.setOnClickListener {
            startActivity(Intent(v!!.context, SplashActivity::class.java))
            act!!.finish()
        }

        changeBtn.setOnClickListener {
            startActivity(Intent(v!!.context, SettingsActivity::class.java))
        }

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