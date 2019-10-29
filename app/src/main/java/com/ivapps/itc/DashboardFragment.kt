package com.ivapps.itc


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val PREF_NAME = getString(R.string.pref_name)
        val prefs = view.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        view.findViewById<TextView>(R.id.dash_name).text = prefs.getString("name", null)
        view.findViewById<TextView>(R.id.dash_dept).text = prefs.getString("department", null)
        view.findViewById<TextView>(R.id.dash_email).text = prefs.getString("email", null)
        view.findViewById<TextView>(R.id.dash_phone).text = prefs.getInt("phone", 0).toString()

        return view
    }


}
