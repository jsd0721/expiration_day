package com.example.expiraton_date

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expiraton_date.databinding.ActivityRegisterBinding
import com.google.zxing.integration.android.IntentIntegrator

class RegisterChoiseActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var registerByBarcodeButton : ImageButton
    private lateinit var registerByDirectButton : Button

    private val registerActivityBinding : ActivityRegisterBinding by lazy{
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(registerActivityBinding.root)

        bindView()
    }

    private fun bindView() {
        registerByBarcodeButton = registerActivityBinding.barcodeReg
        registerByDirectButton = registerActivityBinding.directReg

        registerByDirectButton.setOnClickListener(this)
        registerByBarcodeButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            registerByBarcodeButton->{
                //세로모드 바코드 리더
                val integrator = IntentIntegrator(this@RegisterChoiseActivity)
                integrator.setPrompt("바코드를 읽혀 주세요")
                integrator.setBeepEnabled(false)
                integrator.setOrientationLocked(false)
                integrator.initiateScan()
            }
            registerByDirectButton->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}