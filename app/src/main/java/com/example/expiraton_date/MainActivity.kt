package com.example.expiraton_date

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.expiraton_date.databinding.ActivityMainBinding
import com.example.expiraton_date.sqliteRoom.ProductDatabase
import com.example.expiraton_date.sqliteRoom.ProductTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var myadapter: RcViewAdapter? = null

    //넣을 값들 변수 설정
    private lateinit var productImage : ImageView
    private lateinit var productName: EditText
    private lateinit var expirationDateYear: EditText
    private lateinit var expirationDateMonth: EditText
    private lateinit var expirationDateDay: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var savePlaceSpinner: Spinner
    private lateinit var saveButton: Button

    private val database by lazy{
        ProductDatabase.getInstance(applicationContext)
    }

    private val permissionList by lazy{
        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA)
    }

    //viewBinding
    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkPermission()
        bindView()
    }

    private fun bindView() {
        //xml상에서의 개체들과 각 변수들 연결
        productImage = binding.imageView
        productName = binding.productNameEditText
        expirationDateYear = binding.yearEditText
        expirationDateMonth = binding.monthEditText
        expirationDateDay = binding.dayEditText
        categorySpinner = binding.categorySpinner
        savePlaceSpinner = binding.savePlaceSpinner
        saveButton = binding.buttonSave

        //세이브 버튼 눌렀을때 작동 구현
        saveButton.setOnClickListener(this)
    }

//권한 설정 부분.
    private fun checkPermission(){
        for(permission in permissionList){
            if(ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_GRANTED){
                continue
            }else{
                requestPermission()
            }
        }
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this,permissionList,0x000001)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            0x000001->{
                if(grantResults.isNotEmpty() &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Log.d("TAG","$permissions : $grantResults")
                }else{
                    Toast.makeText(this,"권한 허용이 필요합니다.",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onClick(view: View?) {
        val dateformat = SimpleDateFormat("yyyy-mm-dd")
        val imageValue = productImage.imageAlpha
        val productNameValue = productName.text.toString()
        val expirationDate = "${expirationDateYear.text.toString()}-${expirationDateMonth.text.toString()}-${expirationDateDay.text.toString()}"
        val categoryValue = categorySpinner.selectedItem.toString()
        val savedPlaceValue = savePlaceSpinner.selectedItem.toString()

        val productDTO = ProductTable(
            productName = productNameValue,
            productExpirationDate = expirationDate,
            category = categoryValue,
            savePlace = savedPlaceValue,
            productImage = imageValue
        )

        when(view){
            productImage->{

            }
            saveButton->{
                val builder = AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("저장하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener {_, _ ->
                        CoroutineScope(Dispatchers.IO).launch{
                            try{
                                database!!.productTableDao().saveProduct(productDTO)
                            }catch(err : Exception){
                                Log.d("Error",err.toString())
                            }
                            finish()
                        }
                        Toast.makeText(this,"저장되었습니다",Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("취소",DialogInterface.OnClickListener{_,_->
                        Toast.makeText(this,"취소되었습니다.",Toast.LENGTH_SHORT).show()
                    })
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }

    }
}