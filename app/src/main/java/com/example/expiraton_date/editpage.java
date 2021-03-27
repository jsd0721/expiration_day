package com.example.expiraton_date;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class editpage extends AppCompatActivity {

    Cursor mcursor;
    public Customadapter customadapter;

    Button modify_button;
    Button delete_button;
    public  static ListView prview;
    String selected_product;
    TextView empty_view;

    int position_value;

    String dbName = "product_db.db";
    int dbVersion = 3;
    private MysqliteOpenHelper helper;
    private SQLiteDatabase db;
    String tag = "SQLite";
    String tableName = "product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpage);

        modify_button = (Button) findViewById(R.id.modify);
        delete_button = (Button) findViewById(R.id.delete);
        prview = (ListView) findViewById(R.id.view_pr);
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
        prview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prview.setSelector(new PaintDrawable(0xff808080));
                mcursor.moveToPosition(position);
                selected_product = mcursor.getString(mcursor.getColumnIndex("_id"));
                position_value = position;
            }
        });
        selectDB();
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(selected_product == null)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                            editpage.this);

                    // 제목셋팅
                    dialog.setTitle("삭제");

                    // AlertDialog 셋팅
                    dialog
                            .setMessage("정말로 삭제하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("삭제",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            String sql = "DELETE FROM product where _id = '" + selected_product + "';";
                                            db.execSQL(sql);
                                            selectDB();
                                            Toast.makeText(editpage.this, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .setNegativeButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                } else {
                    return;
                }
            }
        });


        modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(selected_product == null)) {
                    Intent it1 = new Intent(editpage.this, modify.class);
                    it1.putExtra("i", position_value);
                    it1.putExtra("id", selected_product);
                    startActivity(it1);
                }
                else
                    return;
            }
        });
    }

    private void selectDB() {
        db = helper.getWritableDatabase();
        mcursor = (Cursor) db.rawQuery("SELECT*FROM product;", null);
        if (mcursor.getCount() > 0) {
            customadapter = new Customadapter(this, mcursor);
                prview.setAdapter(customadapter);
                customadapter.notifyDataSetChanged();
        } else {
            customadapter = new Customadapter(this, mcursor);
            prview.setAdapter(customadapter);
            empty_view = (TextView) findViewById(R.id.empty_view);
            prview.setEmptyView(empty_view);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewDatabaseActivity.class);
        intent.setFlags(getIntent().FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setFlags(getIntent().FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
