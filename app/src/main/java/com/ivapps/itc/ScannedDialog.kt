package com.ivapps.itc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ivapps.itc.R
import com.ivapps.itc.helpers.EncryptionHelper
import com.ivapps.itc.utils.User

class ScannedDialog(val scannedString:String) : DialogFragment() {

    val TAG = "ScannedDialog"
    private var toolbar: Toolbar? = null
    private var v: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.dialog_scanned, container, false)

        toolbar = v!!.findViewById(R.id.scanned_toolbar)
        toolbar!!.setNavigationIcon(R.drawable.ic_close_white_24dp)
        toolbar!!.setNavigationOnClickListener {
            dialog!!.dismiss()
        }
        toolbar!!.title = "Scan Result"


        val decryptedString = EncryptionHelper.getInstance().getDecryptionString(scannedString)
        val user = Gson().fromJson(decryptedString, User::class.java)

        v!!.findViewById<TextView>(R.id.s_name).text = user.myname
        v!!.findViewById<TextView>(R.id.s_dept).text = user.dept
        v!!.findViewById<TextView>(R.id.s_phone).text = user.phone.toString()
        v!!.findViewById<TextView>(R.id.s_email).text = user.email

        v!!.findViewById<TextView>(R.id.addcontact_btn).setOnClickListener {
            Snackbar.make(v!!.findViewById<RelativeLayout>(R.id.scanned_root),"Add Contact",Snackbar.LENGTH_LONG).show()
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