package com.example.expiraton_date;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewDatabaseActivity extends AppCompatActivity {

    SQLiteDatabase db;
    SQLiteOpenHelper helper;
    Cursor mcursor;
    public Customadapter customadapter;
    TextView empty_view;

    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;

    String databasename = "product_db.db";
    String tablename = "product";
    public ListView view_sql;
    int dbVersion = 3;
    private long backPressedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);

        //버튼들 연결
        Button regButton = (Button)findViewById(R.id.reg_button);
        Button editbutton = (Button)findViewById(R.id.edit_button);
        Button inquirebutton = (Button)findViewById(R.id.Inquire_button);
        //view들 연결
        view_sql = (ListView)findViewById(R.id.product_list);
        empty_view = (TextView) findViewById(R.id.empty_view2);

        regButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewDatabaseActivity.this,register.class);
                startActivity(intent);
            }
        });

        helper = new MysqliteOpenHelper(this, databasename, null, dbVersion);

        editbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),editpage.class);
                startActivity(intent);
            }
        });
        inquirebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Inquire.class);
                startActivity(intent);
            }
        });
        selectDB();
    }
    private void selectDB(){
        db = helper.getWritableDatabase();
        mcursor = (Cursor) db.rawQuery("SELECT*FROM product;",null);
        if(mcursor.getCount()>0){
            customadapter = new Customadapter(this,mcursor);
            view_sql.setAdapter(customadapter);
            customadapter.notifyDataSetChanged();
        }
        else
        {
            view_sql.setEmptyView(empty_view);
        }
    }
    public void onBackPressed(){

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && 2000 >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

        }
    }
}