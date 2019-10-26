package com.ivapps.itc

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generateQrCodeButton.setOnClickListener {
            if (checkEditText()) {
                hideKeyboard()
                val user = UserObject(fullName = fullNameEditText.text.toString(), age = Integer.parseInt(ageEditText.text.toString()))
                val serializeString = Gson().toJson(user)
                val encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg()
                setImageBitmap(encryptedString)
            }
        }

    }

    private fun setImageBitmap(encryptedString: String?) {
        val bitmap = QRCodeHelper.newInstance(this).setContent(encryptedString).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).qrcOde
        qrCodeImageView.setImageBitmap(bitmap)
    }

    /**
     * Hides the soft input keyboard if it is shown to the screen.
     */

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun checkEditText(): Boolean {
        if (TextUtils.isEmpty(fullNameEditText.text.toString())) {
            Toast.makeText(this, "fullName field cannot be empty!", Toast.LENGTH_SHORT).show()
            return false
        } else if (TextUtils.isEmpty(ageEditText.text.toString())) {
            Toast.makeText(this, "age field cannot be empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}
