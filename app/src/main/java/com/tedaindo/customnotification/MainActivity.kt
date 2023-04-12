package com.tedaindo.customnotification

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvToken: TextView
    private lateinit var btnSubscribe: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        setupAction()
    }

    private fun setupAction() {
        btnSubscribe.setOnClickListener(this)
    }

    private fun initUI() {
        tvToken = findViewById(R.id.tv_token)
        btnSubscribe = findViewById(R.id.btn_subscribe)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onClick(p0: View?) {
        when (p0) {
            btnSubscribe -> checkPermission()
        }
    }

    private fun checkPermission(
    ) {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_MEDIA_LOCATION,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            Manifest.permission.VIBRATE,
            Manifest.permission.WAKE_LOCK,
        ).withListener(object : MultiplePermissionsListener {
            @SuppressLint("StringFormatInvalid")
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    Firebase.messaging.subscribeToTopic("teda").addOnCompleteListener { task ->
                        var msg = getString(R.string.msg_subscribed)
                        if (!task.isSuccessful) {
                            msg = getString(R.string.msg_subscribe_failed)
                        }
                        Log.d(TAG, msg)
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    }

                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                            return@OnCompleteListener
                        }

                        // Get new FCM registration token
                        val token = task.result
                        tvToken.text = token
                        // Put token to prefs
//                    prefs.token_firebase = token

                        // Log and toast
                        val msg = getString(R.string.msg_token_fmt, token)
                        Log.d(TAG, msg)
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    })
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermissions()
            }

        }).onSameThread().check()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage(resources.getString(R.string.pesan_alasan))
            .setPositiveButton(resources.getString(R.string.lbl_pengaturan)) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                    exitProcess(0)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton(resources.getString(R.string.lbl_batal)) { _, _ ->
                exitProcess(0)
            }.show()
    }
}