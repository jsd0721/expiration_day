package com.example.expiraton_date

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.expiraton_date.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val binding : ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var startButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        startButton = binding.startButton
        startButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val mainActivityIntent : Intent = Intent(this,ViewDatabaseActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }
}