package com.ivapps.itc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(settings_toolbar)
        supportActionBar!!.title = "Settings"

        fragmentManager.beginTransaction().replace(R.id.settings_frame,SettingsFragment()).commit()

    }
}
