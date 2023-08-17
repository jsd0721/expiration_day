package com.example.expiraton_date

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expiraton_date.databinding.ActivityModifyBinding

class ModifyActivity : AppCompatActivity(),OnClickListener {

    /*Components*/
    private lateinit var categorySpinner: Spinner
    private lateinit var savePlaceSpinner: Spinner
    private lateinit var modifyBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var productNameEditText: EditText
    private lateinit var expirationDateEditText: EditText
    private lateinit var itemImageView : ImageView

    private val binding : ActivityModifyBinding by lazy{
        ActivityModifyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //xml에 객체 연결
        categorySpinner = binding.categorySpinner
        savePlaceSpinner = binding.savePlaceSpinner
        productNameEditText = binding.productNameEditText
        expirationDateEditText = binding.yearEditText
        modifyBtn = binding.buttonMod
        cancelBtn = binding.buttonCancel
        itemImageView = binding.imageView

        modifyBtn.setOnClickListener(this)
        cancelBtn.setOnClickListener(this)
    }

    override fun onClick(button: View?) {
        when(button){
            modifyBtn->{
                val nameValue = productNameEditText.text.toString()
                val dateValue = expirationDateEditText.text.toString()
                val categoryValue = categorySpinner.selectedItem.toString()
                val savePlaceValue = savePlaceSpinner.selectedItem.toString()

                if ("" == nameValue || "" == dateValue || "" == categoryValue || "" == savePlaceValue) {
                    Toast.makeText(this@ModifyActivity, "항목을 모두 입력하십시오", Toast.LENGTH_SHORT).show()
                } else {

                }
            }
            cancelBtn-> finish()
        }
    }
}