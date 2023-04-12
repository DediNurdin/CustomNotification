package com.tedaindo.customnotification.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.tedaindo.customnotification.R

class PhoneActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvVerifiedOTP: TextView
    private lateinit var etPhone: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)

        initUI()
        setupAction()
    }

    private fun setupAction() {
        tvVerifiedOTP.setOnClickListener(this)
    }

    private fun initUI() {

        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )

        tvVerifiedOTP = findViewById(R.id.tv_verif_fg_profile)
        etPhone = findViewById(R.id.et_phone_fg_profile)
    }

    override fun onClick(v: View) {
        when (v) {
            tvVerifiedOTP -> {
                if (etPhone.text.toString() == "") {
                    Toast.makeText(
                        this,
                        "nomor telepon tidak boleh kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    AlertDialog.Builder(this)
                        .setMessage(R.string.pesan_konfir_telepon)
                        .setPositiveButton(R.string.lbl_ok) { dialog, _ ->
                            var phoneFinal = ""
                            phoneFinal = if (etPhone.text.toString().subSequence(0, 2) == "62") {
                                "+62" + etPhone.text.toString()
                                    .subSequence(2, etPhone.text.toString().length)
                            } else {
                                "+62" + etPhone.text.toString()
                            }
                            val intent = Intent(this, OTPActivity::class.java)
                            intent.putExtra("phone", phoneFinal)
                            startActivity(intent)
                            // time count down for 30 seconds, // with 1 second as countDown interval
                        }
                        .setNegativeButton(R.string.lbl_batal) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
    }
}