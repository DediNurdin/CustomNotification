package com.tedaindo.customnotification.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.tedaindo.customnotification.MainActivity
import com.tedaindo.customnotification.R

class UtamaActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnLatOne : Button
    private lateinit var btnLatTwo : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utama)

        initUI()
        setupAction()
    }

    private fun setupAction() {
        btnLatOne.setOnClickListener(this)
        btnLatTwo.setOnClickListener(this)
    }

    private fun initUI() {
        btnLatOne = findViewById(R.id.btn_lat_1)
        btnLatTwo = findViewById(R.id.btn_lat_2)
    }


    override fun onClick(v: View) {
        when(v) {
            btnLatOne -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            btnLatTwo -> {
                val intent = Intent(this, PhoneActivity::class.java)
                startActivity(intent)
            }
        }
    }

}