package com.example.expiraton_date;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Customadapter myadapter;
    //데이터베이스 변수 설정
    String dbName = "product_db.db";
    int dbVersion = 3;
    private MysqliteOpenHelper helper;
    private SQLiteDatabase db;
    String tag = "SQLite";
    String tableName = "product";

    //넣을 값들 변수 설정
    EditText Product_Name;
    EditText Expiration_Date;
    Spinner Pr_Category;
    Spinner Save_Place;

    Button Save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //xml상에서의 개체들과 각 변수들 연결
        Product_Name = (EditText) findViewById(R.id.product_name);
        Expiration_Date = (EditText) findViewById(R.id.expiraton_date);
        Pr_Category = (Spinner) findViewById(R.id.spinner_category);
        Save_Place = (Spinner) findViewById(R.id.save_place_spinner);
        Save_button = (Button)  findViewById(R.id.button_save);

        //database 선언
        helper = new MysqliteOpenHelper(
                this,
                dbName,
                null,
                dbVersion);

        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(tag, "DB를 열 수 없음");
            finish();
        }
        //세이브 버튼 눌렀을때 작동 구현
        Save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getName = Product_Name.getText().toString();
                String getExDate = Expiration_Date.getText().toString();
                String getCategory = Pr_Category.getSelectedItem().toString();
                String getSavePlace = Save_Place.getSelectedItem().toString();


                if ("".equals(getName) || "".equals(getExDate) ||"".equals(getCategory)||"".equals(getSavePlace)) {
                    Toast.makeText(v.getContext(), "항목을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
                Date Ed = null;
                try {
                    Ed = SDF.parse(getExDate);
                }catch (ParseException e) {
                    Toast.makeText(v.getContext(), "yyyy-mm-dd형식으로 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                insert(getName, getExDate, getCategory, getSavePlace);
            }
        });
    }

    void insert(String getName, String getExDate, String getCategory, String getSavePlace) {
        ContentValues values = new ContentValues();
        View v = new View(this);
        values.put("product_name", getName);
        values.put("expiration_date", getExDate);
        values.put("category", getCategory);
        values.put("save_place", getSavePlace);
        long result = db.insert(tableName,null,values);
        Log.d(tag, result + "번째 저장됨");
        Toast.makeText(v.getContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ViewDatabaseActivity.class);
        intent.setFlags(getIntent().FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setFlags(getIntent().FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}