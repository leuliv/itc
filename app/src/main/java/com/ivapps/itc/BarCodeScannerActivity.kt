package com.ivapps.itc

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_bar_code_scanner.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class BarCodeScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    companion object {
        private const val SAMSUNG = "samsung"
        private const val MY_CAMERA_REQUEST_CODE = 6515
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_code_scanner)
        setScannerProperties()
        go_back_icon.setOnClickListener { onBackPressed() }
        flash_icon.setOnClickListener {
            if (qr_scanner.flash) {
                qr_scanner.flash = false
                flash_icon.background = ContextCompat.getDrawable(this, R.drawable.flash_off_vector_icon)
            } else {
                qr_scanner.flash = true
                flash_icon.background = ContextCompat.getDrawable(this, R.drawable.flash_on_vector_icon)
            }
        }

    }

    private fun setScannerProperties() {
        qr_scanner.setFormats(listOf(BarcodeFormat.QR_CODE))
        qr_scanner.setAutoFocus(true)
        qr_scanner.setLaserColor(R.color.colorAccent)
        qr_scanner.setMaskColor(R.color.colorAccent)
        if (Build.MANUFACTURER.equals(SAMSUNG, ignoreCase = true))
            qr_scanner.setAspectTolerance(0.5f)
    }

    /**
     * resume the qr code camera when activity is in onResume state.
     */

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                    MY_CAMERA_REQUEST_CODE)
                return
            }
        }
        qr_scanner.startCamera()
        qr_scanner.setResultHandler(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openCamera()
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                showCameraSnackBar()
        }
    }

    private fun showCameraSnackBar() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            val snackbar = Snackbar.make(scanner_root, resources.getString(R.string.app_needs_your_camera_permission_in_order_to_scan_qr_code), Snackbar.LENGTH_LONG)
            val view1 = snackbar.view
            view1.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            val textView = view1.findViewById<TextView>(R.id.snackbar_text)
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            snackbar.show()
        }
    }

    private fun openCamera() {
        qr_scanner.startCamera()
        qr_scanner.setResultHandler(this)
    }

    override fun onPause() {
        super.onPause()
        qr_scanner.stopCamera()
    }

    override fun handleResult(p0: Result?) {
        if (p0 != null) {
            val dialog = ScannedDialog(p0.text)
            val ft = supportFragmentManager.beginTransaction()
            dialog.show(ft, dialog.TAG)
            //startActivity(ScannedActivity.getScannedActivity(this, p0.text))
            resumeCamera()
        }
    }

    private fun resumeCamera() {
        Toast.LENGTH_LONG
        val handler = Handler()
        handler.postDelayed({ qr_scanner.resumeCameraPreview(this) }, 2000)
    }
}
