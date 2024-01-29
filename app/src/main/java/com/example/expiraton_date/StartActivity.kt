package com.example.expiraton_date

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.edit
import com.example.expiraton_date.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var startButton : Button
    private val mainActivityIntent by lazy{
        Intent(this,ViewDatabaseActivity::class.java)
    }
    private val binding : ActivityStartBinding by lazy{
        ActivityStartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//      이전에 방문했던 사용자라면 그냥 화면 넘기기
        if(getSharedPreferences("startValue", MODE_PRIVATE).getInt("value",0) == 1){
            startActivity(mainActivityIntent)
            finish()
        }else{
            startButton = binding.startButton
            startButton.setOnClickListener(this)
        }
    }

    override fun onClick(p0: View?) {
        val preference = getSharedPreferences("startValue",MODE_PRIVATE)
        preference.edit(){
            this.putInt("value",1)
            commit()
        }
        startActivity(mainActivityIntent)
        finish()
    }
}