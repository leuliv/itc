package com.ivapps.itc

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivapps.itc.db.RetrofitClient
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    var PREF_NAME = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        PREF_NAME = getString(R.string.pref_name)


        login_btn.setOnClickListener {
            val phone = login_phone.editableText.toString().replace("+251", "").replace("+", "")
            if (phone.isNotEmpty()) {
                login_phone.error = null
                login(phone.toInt())
            } else {
                error(login_phone)
            }
        }

        goto_register_btn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

    }

    private fun login(phone: Int) {
        val call = RetrofitClient.getInstance(this).api.login(phone)
        call.enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    val obj = JSONObject(Gson().toJson(response.body()))
                    val s = obj.getString("status")
                    val name = obj.getString("name")
                    val dept = obj.getString("department")
                    val email = obj.getString("email")
                    when (s) {
                        "ok" -> {
                            Snackbar.make(
                                login_root,
                                "Member Logged in!",
                                Snackbar.LENGTH_LONG
                            ).show()
                            val editor =
                                getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                            editor.putString("name", name)
                            editor.putString("department", dept)
                            editor.putString("email", email)
                            editor.putInt("phone", phone)
                            editor.apply()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                        "failed" -> Toast.makeText(
                            this@LoginActivity,
                            "Log in failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@LoginActivity,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }

    private fun error(view: EditText) {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        view.error = "Fill in your phone"
        view.startAnimation(shake)
    }
}
