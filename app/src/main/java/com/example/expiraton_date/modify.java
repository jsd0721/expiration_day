package com.example.expiraton_date;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class modify extends AppCompatActivity {

    Cursor mcursor;
    Spinner category;
    Spinner SavePlace;
    Button Modify;
    Button Cancel;
    EditText Product_Name;
    EditText Expiration_Date;
    int position;

    Customadapter myadapter;

    SQLiteOpenHelper helper;
    SQLiteDatabase db;

    String DBname = "product_db.db";
    int DBversion = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        helper = new MysqliteOpenHelper(
                this,
                DBname,
                null,
                DBversion);
        try{
             db = helper.getWritableDatabase();}
        catch (SQLiteException e) {
            e.printStackTrace();
            finish();
        }


        //xml에 객체 연결
        category = (Spinner)findViewById(R.id.spinner_category);
        SavePlace = (Spinner)findViewById(R.id.save_place_spinner);
        Product_Name = (EditText)findViewById(R.id.product_name);
        Expiration_Date = (EditText)findViewById(R.id.expiraton_date);
        Modify = (Button)findViewById(R.id.button_mod);
        Cancel = (Button)findViewById(R.id.button_cancel);

        //editpage에서 받은 position값 설정
        position = getIntent().getIntExtra("i",65535);

        String sql = "select * from product";
        mcursor = db.rawQuery(sql,null);
            mcursor.moveToPosition(position);
            Product_Name.setText(mcursor.getString(mcursor.getColumnIndex("product_name")));
            Expiration_Date.setText(mcursor.getString(mcursor.getColumnIndex("expiration_date")));


        Modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Product_Name.getText().toString();
                String date = Expiration_Date.getText().toString();
                String category_mod = category.getSelectedItem().toString();
                String saveplace_mod = SavePlace.getSelectedItem().toString();
                if("".equals(name)||"".equals(date)||"".equals(category_mod)||"".equals(saveplace_mod)){
                    Toast.makeText(modify.this,"항목을 모두 입력하십시오",Toast.LENGTH_SHORT).show();
                }

                else {
                    String id = getIntent().getStringExtra("id");

                    String sql = "update product set product_name ='" + name + "' where _id = '" + id + "';'";
                    db.execSQL(sql);
                    sql = "update product set expiration_date ='" + date + "' where _id = '" + id + "';'";
                    db.execSQL(sql);
                    sql = "update product set category ='" + category_mod + "' where _id = '" + id + "';'";
                    db.execSQL(sql);
                    sql = "update product set save_place ='" + saveplace_mod + "' where _id = '" + id + "';'";
                    db.execSQL(sql);
                    Toast.makeText(modify.this, "저장되었습니다", Toast.LENGTH_SHORT).show();

                    myadapter = new Customadapter(modify.this, mcursor);
                    editpage.prview.setAdapter(myadapter);
                    Intent inte = new Intent(modify.this, editpage.class);
                    startActivity(inte);
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
