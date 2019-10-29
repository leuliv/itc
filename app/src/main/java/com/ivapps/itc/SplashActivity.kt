package com.ivapps.itc

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivapps.itc.db.RetrofitClient
import com.ivapps.itc.utils.User
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    val user = User()
    var PREF_NAME = ""
    var myIp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setUpPrefs()

        loading_text.text = getString(R.string.connecting)

        val call = RetrofitClient.getInstance(this).api.checkConn()
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                if (user.phone != 0) {
                    loading_text.text = getString(R.string.logging_in)
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                } else {
                    loading_text.text = getString(R.string.network_error)
                    val dialog = NoInternetDialog(t.message.toString())
                    val ft = supportFragmentManager.beginTransaction()
                    dialog.show(ft, dialog.TAG)
                }
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loading_text.text = getString(R.string.checking_response)
                try {
                    val obj = JSONObject(Gson().toJson(response.body()))
                    val s = obj.getString("status")
                    when (s) {
                        "ok" -> {
                            loading_text.text = getString(R.string.response_successful)
                            if (user.phone != 0) {
                                loading_text.text = getString(R.string.logging_in)
                                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                                finish()
                            } else {
                                loading_text.text = getString(R.string.no_user_found_going_to_register)
                                startActivity(Intent(this@SplashActivity, RegisterActivity::class.java))
                                finish()
                            }
                        }
                        "error" -> {
                            loading_text.text = getString(R.string.response_unsuccessful)
                            val dialog = NoInternetDialog("Network Error,Close app and retry")
                            val ft = supportFragmentManager.beginTransaction()
                            dialog.show(ft, dialog.TAG)
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@SplashActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }

        })

    }

    private fun setUpPrefs() {
        PREF_NAME = getString(R.string.pref_name)
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        user.myname = prefs.getString("name", null)
        user.dept = prefs.getString("department", null)
        user.email = prefs.getString("email", null)
        user.phone = prefs.getInt("phone", 0)
        if (prefs.getBoolean("firstRun", true)){
            val editor = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putString("ip","192.168.43.210")
            editor.putBoolean("firstRun",false)
            editor.apply()
        }else{
            myIp = prefs.getString("ip", "").toString()
        }
    }
}
