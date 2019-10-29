package com.ivapps.itc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.ivapps.itc.helpers.EncryptionHelper
import com.ivapps.itc.helpers.QRCodeHelper
import com.ivapps.itc.utils.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mToggle: ActionBarDrawerToggle? = null
    val user = User()
    var prevMenuItem: MenuItem? = null
    var PREF_NAME = ""
    var myIp = ""

    //Main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ToolBar Stuff
        setSupportActionBar(main_toolbar)
        supportActionBar!!.apply {
            setDisplayShowCustomEnabled(true)
        }
        //Toolbar and Drawer
        mToggle =
            ActionBarDrawerToggle(this, drawer_layout, main_toolbar, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(mToggle!!)
        mToggle!!.syncState()
        //Drawer Header
        val header = nav_drawer.getHeaderView(0)
        header.findViewById<TextView>(R.id.header_name).text = user.myname
        header.findViewById<TextView>(R.id.header_dept).text = user.dept

        //ViewPager
        main_viewpager.adapter = ViewPagerAdapter(supportFragmentManager)
        main_bottomnav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard_menu -> main_viewpager.currentItem = 0
                R.id.projects_menu -> main_viewpager.currentItem = 1
            }
            false
        }
        main_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                refresh()
                when (position) {
                    0 -> supportActionBar!!.title = getString(R.string.dashboard)
                    1 -> supportActionBar!!.title = getString(R.string.projects)
                }
            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null)
                    prevMenuItem!!.isChecked = false
                else
                    main_bottomnav.menu.getItem(0).isChecked = false

                if (position > 1){
                    main_bottomnav.menu.getItem(0).isChecked = true
                    prevMenuItem = main_bottomnav.menu.getItem(0)
                }else{
                    main_bottomnav.menu.getItem(position).isChecked = true
                    prevMenuItem = main_bottomnav.menu.getItem(position)
                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        //Navigation Listener
        nav_drawer.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.settings_nav -> {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                    true
                }
                R.id.about_nav -> {
                    val dialog = AboutDialog()
                    val ft = supportFragmentManager.beginTransaction()
                    dialog.show(ft, dialog.TAG)
                    true
                }
                R.id.help_nav -> {
                    val dialog = HelpDialog()
                    val ft = supportFragmentManager.beginTransaction()
                    dialog.show(ft, dialog.TAG)
                    true
                }
                R.id.logout_nav -> {
                    signOut()
                    true
                }
                else -> false
            }
        }
    }

    //Set Up Preferences
    private fun setUpPrefs() {
        PREF_NAME = getString(R.string.pref_name)
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        user.myname = prefs.getString("name", null)
        user.dept = prefs.getString("department", null)
        user.email = prefs.getString("email", null)
        user.phone = prefs.getInt("phone", 0)
        myIp = prefs.getString("ip", "").toString()
    }

    //On Activity Start
    override fun onStart() {
        super.onStart()
        refresh()
    }

    //On Activity Resume
    override fun onResume() {
        super.onResume()
        refresh()
    }

    //Refresh Data
    private fun refresh() {
        setUpPrefs()
        if (user.phone != 0) {

        } else {
            startActivity(Intent(this@MainActivity, SplashActivity::class.java))
            finish()
        }
    }

    inner class ViewPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> DashboardFragment()
                1 -> ProjectsFragment()
                else -> ErrorFragment()
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.myqr -> {
                showQRLayout()
                true
            }
            R.id.scanqr -> {
                startActivity(Intent(this, BarCodeScannerActivity::class.java))
                true
            }
            R.id.my_projs -> {
                val dialog = MyProjsDialog()
                val ft = supportFragmentManager.beginTransaction()
                dialog.show(ft, dialog.TAG)
                true
            }
            else -> false
        }
    }

    private fun showQRLayout() {
        val alertDialog = AlertDialog.Builder(this).create()
        val layout =
            layoutInflater.inflate(
                R.layout.show_qr_layout,
                findViewById(R.id.showqr_root)
            )

        alertDialog.setCancelable(true)
        alertDialog.setView(layout)

        val qrImage = layout.findViewById<ImageView>(R.id.qrCode_image)
        setUpPrefs()
        val serializeString = Gson().toJson(user)
        val encryptedString =
            EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg()

        val bitmap =
            QRCodeHelper.newInstance(this).setContent(encryptedString).setErrorCorrectionLevel(
                ErrorCorrectionLevel.Q
            ).setMargin(2).qrcOde
        qrImage.setImageBitmap(bitmap)

        alertDialog.show()

    }

    private fun signOut() {
        val alertDialog = AlertDialog.Builder(this).create()

        alertDialog.setTitle("Sign Out")
        alertDialog.setMessage("Are you sure you want to Sign Out?")
        alertDialog.setCancelable(false)

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
            try {
                // clearing app data
                val packageName = this.packageName
                val runtime = Runtime.getRuntime()
                runtime.exec("pm clear $packageName")

            } catch (e: Exception) {
                e.printStackTrace()
            }
            alertDialog.dismiss()
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { _, _ ->
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}
