package com.ivapps.itc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivapps.itc.db.RetrofitClient
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    var PREF_NAME = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        PREF_NAME = getString(R.string.pref_name)


        register_btn.setOnClickListener {
            val name = reg_name.editableText.toString()
            val dept = reg_dept.editableText.toString()
            var email = reg_email.editableText.toString()
            val phone = reg_phone.editableText.toString().replace("+251", "").replace("+", "")

            if (name.isNotEmpty() && dept.isNotEmpty() && phone.isNotEmpty()) {
                if (email.isEmpty())
                    email = "non"
                register(name, dept, email, phone.toInt())
            } else {
                if (name.isEmpty()) {
                    error(reg_name, "Fill in your name")
                } else {
                    reg_name.error = null
                }

                if (dept.isEmpty()) {
                    error(reg_dept, "Fill in your department")
                } else {
                    reg_dept.error = null
                }

                if (phone.isEmpty()) {
                    error(reg_phone, "Fill in your phone")
                } else {
                    reg_phone.error = null
                }
            }
        }

        goto_login_btn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

    }

    private fun register(name: String, dept: String, email: String, phone: Int) {
        try {
            val call = RetrofitClient.getInstance(this).api.register(name, dept, email, phone)
            call.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        val obj = JSONObject(Gson().toJson(response.body()))
                        val s = obj.getString("status")
                        when (s) {
                            "ok" -> {
                                Snackbar.make(
                                    register_root,
                                    "Member Registereed",
                                    Snackbar.LENGTH_LONG
                                ).show()
                                val editor =
                                    getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                                editor.putString("name", name)
                                editor.putString("department", dept)
                                editor.putString("email", email)
                                editor.putInt("phone", phone)
                                editor.apply()
                                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                                finish()
                            }
                            "exists" -> Toast.makeText(
                                this@RegisterActivity,
                                "Member Exists",
                                Toast.LENGTH_SHORT
                            ).show()
                            else -> Toast.makeText(
                                this@RegisterActivity,
                                "Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@RegisterActivity,
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } catch (e: Exception) {
            Toast.makeText(
                this@RegisterActivity,
                e.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun error(view: EditText, error: String) {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        view.error = error
        view.startAnimation(shake)
    }

}
