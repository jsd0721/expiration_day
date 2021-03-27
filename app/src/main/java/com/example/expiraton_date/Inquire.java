package com.example.expiraton_date;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class Inquire extends AppCompatActivity {

    Spinner _category;
    Spinner _SavePlace;
    ListView _ViewSelectedCategory;
    Button searchbutton;
    EditText _SearchWindow;

    int dbVersion = 3;
    private MysqliteOpenHelper helper;
    private SQLiteDatabase db;
    String dbname  = "product_db.db";
    String tableName = "product";

    Cursor mcursor;
    private Customadapter customadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire);

        _category = (Spinner)findViewById(R.id.spinner_food);
        _SavePlace = (Spinner)findViewById(R.id.spinner_save);
        _ViewSelectedCategory = (ListView)findViewById(R.id.listview);
        searchbutton = (Button)findViewById(R.id.button);
        _SearchWindow = (EditText)findViewById(R.id.editSearch);

        helper = new MysqliteOpenHelper(
                this,
                dbname,
                null,
                dbVersion);

        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getCategory = _category.getSelectedItem().toString();
                final String getSavePlace = _SavePlace.getSelectedItem().toString();
                final String SearchName = _SearchWindow.getText().toString();
                if((getSavePlace.equals("")|| getCategory.equals(""))&& SearchName.equals("")){
                    dialog();
                }
                else {
                    selectDB(getCategory, getSavePlace, SearchName);
                }
            }
        });

    }
    private void selectDB(String cat, String sp, String name){

        db = helper.getWritableDatabase();
        String sql;
        if(!name.equals("")&&cat.equals("")&&sp.equals("")) {
            sql = "SELECT * FROM product where product_name = '" + name + "';";
        }
        else if(name.equals("")&&!cat.equals("")&&!sp.equals("")){
            sql = "SELECT * FROM product where category = '" + cat + "' and save_place = '" + sp + "';";
        }
        else{
            sql = "SELECT * FROM product where product_name = '" + name + "' and category = '" + cat + "' and save_place = '" + sp + "';";
        }
        mcursor = (Cursor) db.rawQuery(sql,null);
        if(mcursor.getCount()>0){
            customadapter = new Customadapter(this,mcursor);
            _ViewSelectedCategory.setAdapter(customadapter);
        }else
        {
            customadapter = new Customadapter(this,mcursor);
            _ViewSelectedCategory.setAdapter(customadapter);
            Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    public void dialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
               this);

        // 제목셋팅
        alertDialogBuilder.setTitle("알림");

        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage("분류를 모두 선택해 주세요!")
                .setCancelable(false)
                .setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                public void onClick(
                        DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
