package com.example.expiraton_date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Customadapter extends CursorAdapter {
    public long left_day;

    public long calculateLeftDate(Date exdate) {
        long today;
        today = System.currentTimeMillis() / (1000 * 60 * 60 * 24);
        long defday = ((exdate.getTime() / (1000 * 60 * 60 * 24)) - today);
        return defday;
    }

    public Customadapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.itemlayout, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final TextView product_name = (TextView) view.findViewById(R.id.prname_layout);
        final TextView expiration_date = (TextView) view.findViewById(R.id.dataItem02);
        final TextView category = (TextView) view.findViewById(R.id.textView3);
        final TextView save_place = (TextView) view.findViewById(R.id.textView2);
        final TextView DAY = (TextView) view.findViewById(R.id.DAYTEXT);

        String Ed = cursor.getString(cursor.getColumnIndex("expiration_date"));
        String strColor;//날짜에 따른 문자 색상 결정
        Date Convert_Ed = null;
        try {
            Convert_Ed = new SimpleDateFormat("yyyy-MM-dd").parse(Ed);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        left_day = calculateLeftDate(Convert_Ed);

        product_name.setText("제품명 : " + cursor.getString(cursor.getColumnIndex("product_name")));
        expiration_date.setText("유통기한 : " + cursor.getString(cursor.getColumnIndex("expiration_date")));
        category.setText("카테고리 : " + cursor.getString(cursor.getColumnIndex("category")));
        save_place.setText("보관 장소 : " + cursor.getString(cursor.getColumnIndex("save_place")));
        DAY.setText(left_day + "일");
        if(left_day < 0){strColor = "#FF0000";}
        else if(left_day<=3&&left_day>=0) {strColor = "#FFD700";}
        else{strColor = "#00FF00";}
        DAY.setTextColor(Color.parseColor(strColor));
    }
}
