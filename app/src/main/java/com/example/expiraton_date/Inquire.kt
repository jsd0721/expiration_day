package com.example.expiraton_date

import android.app.AlertDialog
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expiraton_date.databinding.ActivityInquireBinding

class Inquire : AppCompatActivity(),OnClickListener {

    /*component*/
    private lateinit var findItemRcView : RecyclerView
    private lateinit var categorySpinner: Spinner
    private lateinit var savePlaceSpinner: Spinner
    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText

    /*recyclerView 관련*/
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var rcViewAdapter: RcViewAdapter

    private val binding : ActivityInquireBinding by lazy{
        ActivityInquireBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        categorySpinner = binding.spinnerCategory
        savePlaceSpinner = binding.spinnerSavePlace
        findItemRcView = binding.inquireRcView
        searchButton = binding.searchButton
        searchEditText = binding.editSearch

        searchButton!!.setOnClickListener(this)
    }

    fun dialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        // 제목셋팅
        alertDialogBuilder.setTitle("알림")

        // AlertDialog 셋팅
        alertDialogBuilder
            .setMessage("분류를 모두 선택해 주세요!")
            .setCancelable(false)
            .setPositiveButton("확인") { dialog, id -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onClick(view: View?) {
        when(view){
            searchButton->{
                val getCategory = categorySpinner.selectedItem.toString()
                val getSavePlace = savePlaceSpinner.selectedItem.toString()
                val searchedName = searchEditText.text.toString()

                if ((getSavePlace == "" || getCategory == "") && searchedName == "") {
                    dialog()
                } else {
                    TODO("레트로핏으로 검색된 데이터 가져오는 로직")
                }
            }
        }
    }
}