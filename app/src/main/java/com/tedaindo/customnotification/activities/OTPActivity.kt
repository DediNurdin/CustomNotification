package com.tedaindo.customnotification.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.tedaindo.customnotification.R
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvtitle: TextView
    private lateinit var btSaveDialog: Button
    private lateinit var btCancel: Button
    private lateinit var btResend: Button
    private lateinit var tvTimerDg: TextView
    private lateinit var etOTP: EditText

    private lateinit var obj: String

    // variable for FirebaseAuth class
    private var mAuth: FirebaseAuth? = null

    // string for storing our verification ID
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)

        initUI()
        initData()
        setupActions()
    }

    private fun initUI() {

        mAuth = FirebaseAuth.getInstance()

        tvtitle = findViewById(R.id.tv_phone_verif_otp)
        btSaveDialog = findViewById(R.id.bt_save_verif_otp)
        btCancel = findViewById(R.id.bt_cancel_verif_otp)
        btResend = findViewById(R.id.bt_resend_verif_otp)
        tvTimerDg = findViewById(R.id.tv_timer_verif_otp)
        etOTP = findViewById(R.id.et_verif_otp)
    }

    private fun initData() {
        obj = intent.getStringExtra("phone") as String
        tvtitle.text = "Masukan Kode OTP yang dikirim ke nomor ".plus(obj)
        if (obj != "") {
            sendVerificationCode(obj)
            object : CountDownTimer(60000, 1000) {
                // Callback function, fired on regular interval
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    tvTimerDg.text = "kirim ulang (" + millisUntilFinished / 1000 + ")"
                    tvTimerDg.isEnabled = false
                    // generateOTPBtn?.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
                    // generateOTPBtn?.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.md_grey_500))
                }

                // Callback function, fired // when the time is up
                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    tvTimerDg.text = "Kirim ulang"
                    tvTimerDg.isEnabled = true
//                            generateOTPBtn?.textColors?.defaultColor
//                            generateOTPBtn?.background?.clearColorFilter()
                }
            }.start()
        } else {
            object : CountDownTimer(60000, 1000) {
                // Callback function, fired on regular interval
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    tvTimerDg.text = "kirim ulang (" + millisUntilFinished / 1000 + ")"
                    tvTimerDg.isEnabled = false
                    // generateOTPBtn?.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
                    // generateOTPBtn?.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.md_grey_500))
                }

                // Callback function, fired // when the time is up
                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    tvTimerDg.text = "Kirim ulang"
                    tvTimerDg.isEnabled = true
//                            generateOTPBtn?.textColors?.defaultColor
//                            generateOTPBtn?.background?.clearColorFilter()
                }
            }.start()
        }
    }

    private fun setupActions() {
        btSaveDialog.setOnClickListener(this)
        tvTimerDg.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tvTimerDg -> {
                var phoneFinal = ""
                phoneFinal = if (obj.subSequence(0, 2) == "62") {
                    "+62" + obj
                        .subSequence(2, obj.length)
                } else {
                    "+62$obj"
                }

                object : CountDownTimer(60000, 1000) {
                    @SuppressLint("SetTextI18n")
                    override fun onTick(millisUntilFinished: Long) {
                        tvTimerDg.text = "kirim ulang (" + millisUntilFinished / 1000 + ")"
                        tvTimerDg.isEnabled = false
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onFinish() {
                        tvTimerDg.text = "Kirim ulang"
                        tvTimerDg.isEnabled = true
                    }
                }.start()

                sendVerificationCode(phoneFinal)
            }
            btSaveDialog -> {
                verifyCode(etOTP.text.toString())
            }
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

//                    if (Helpers.isNetworkAvailable(this)) {
//                        showProgress(true)
//                        IdentityRepository().verifyPhone(this, prefs.refreshToken, prefs.id)
//                    } else {
//                        Toast.makeText(
//                            this, resources.getString(
//                                R.string.message_tidak_ada_akses_internet
//                            ), Toast.LENGTH_SHORT
//                        ).show()
//                    }

                    Toast.makeText(
                        this, task.result.toString(), Toast.LENGTH_LONG
                    ).show()

                } else {
                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

//    fun onVerifyPhoneSuccess(result: String) {
//        showProgress(false)
//        prefs.verified = 1
//        startActivity(Intent(this, DashboardActivity::class.java))
//        overridePendingTransition(
//            R.anim.up_out,
//            R.anim.bottom_in
//        )
//        val parentLayout: View = findViewById(android.R.id.content)
//        val snackbar: Snackbar =
//            Snackbar.make(parentLayout, result.toString(), Snackbar.LENGTH_LONG)
//        snackbar.show()
//
//        Toast.makeText(
//            this@VerifOtpActivity, "Verifikasi Berhasil", Toast.LENGTH_LONG
//        ).show()
//    }
//
//    fun onVerifyPhoneFailure(message: String) {
//        showProgress(false)
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }


    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode

                if (code != null) {
                    // if the code is not null then
                    // we are setting that code to
                    // our OTP edittext field.
//                    otpDialog = code
//                    edtOTP!!.setText(code)

                    // after setting this code
                    // to OTP edittext field we
                    // are calling our verifycode method.

//                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@OTPActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)

        signInWithCredential(credential)
    }

    override fun onBackPressed() {

    }
}