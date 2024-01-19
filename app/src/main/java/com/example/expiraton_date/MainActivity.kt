package com.example.expiraton_date

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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


    val REQUEST_IMAGE_CAPTURE = 1

    private val database by lazy{
        ProductDatabase.getInstance(applicationContext)
    }

//   안드로이드 버전 따라 권한 설정하기
    private val permissionList by lazy{
        when(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            true->
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES,android.Manifest.permission.CAMERA)
            false->
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA)
        }
    }

    //viewBinding
    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindView()
    }

    private fun bindView() {
        //xml상에서의 개체들과 각 변수들 연결
        productImage = binding.productImageView
        productName = binding.productNameEditText
        expirationDateYear = binding.yearEditText
        expirationDateMonth = binding.monthEditText
        expirationDateDay = binding.dayEditText
        categorySpinner = binding.categorySpinner
        savePlaceSpinner = binding.savePlaceSpinner
        saveButton = binding.buttonSave

        //세이브 버튼 눌렀을때 작동 구현
        saveButton.setOnClickListener(this)
        productImage.setOnClickListener(this)
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
        requestPermissions(permissionList,0x000001)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            0x000001->{
                if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Log.d("TAG","$permissions : $grantResults")
                }else{
                    Toast.makeText(this,"권한 허용이 필요합니다.",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onClick(view: View?) {

        when(view){
            productImage->{
                checkPermission()
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{
                    takePictureIntent-> takePictureIntent.resolveActivity(packageManager)?.also{
                        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
            saveButton->{

                val dateformat = SimpleDateFormat("yyyy-mm-dd")

                val year = expirationDateYear.text.toString()
                val month = expirationDateMonth.text.toString()
                val day = expirationDateDay.text.toString()

                val imageValue = productImage.imageAlpha
                val productNameValue = productName.text.toString()
                val categoryValue = categorySpinner.selectedItem.toString()
                val savedPlaceValue = savePlaceSpinner.selectedItem.toString()
                val expirationDate = "$year-$month-$day"

//              날짜 텍스트 유효성 검사 후 다음 로직 수행.
                if(productNameValue.equals("")||year.equals("")||month.equals("")||day.equals("")||categoryValue == ""||savedPlaceValue == ""){
                    Toast.makeText(this,"모든 항목을 입력해 주세요",Toast.LENGTH_SHORT).show()
                    return
                }

                if(month.toInt() in 1..12 && day.toInt() in 1..31){

                    val productDTO = ProductTable(
                            productName = productNameValue,
                            productExpirationDate = expirationDate,
                            category = categoryValue,
                            savePlace = savedPlaceValue,
                            productImage = imageValue
                    )

//                  얼럿 다이얼로그 빌더 생성
                    val builder = AlertDialog.Builder(this)
                            .setTitle("알림")
                            .setMessage("저장하시겠습니까?")
//                          긍정 버튼 누르면 데이터베이스에 데이터 저장 후 메시지 띄우고 이전 화면으로 돌아가기
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
//                          부정 버튼 누르면 모든 로직 취소 후 메시지 띄움.
                            .setNegativeButton("취소",DialogInterface.OnClickListener{_,_->
                                Toast.makeText(this,"취소되었습니다.",Toast.LENGTH_SHORT).show()
                            })
                    val alertDialog = builder.create()
                    alertDialog.show()
                }else{
                    Toast.makeText(this,"날짜를 확인해 주세요",Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            productImage.setImageBitmap(imageBitmap)

        }
    }
}