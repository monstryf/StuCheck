package com.example.stucheck;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.stucheck.Service.DataBaseHelper;

import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {
    private ListView sheetList;
    private ArrayAdapter adapter;
    private ArrayList listItems = new ArrayList();
    private long cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        lodaListItem();
        sheetList = findViewById(R.id.sheetList);
        adapter = new ArrayAdapter(this,R.layout.sheet_list,R.id.sheetList,listItems);
    }

    private void lodaListItem() {
        Cursor cursor = new DataBaseHelper(this).getDistinctMonths(cid);
        while (cursor.moveToNext()){
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.DATE_KEY));
            listItems.add(date.substring(3));
        }
    }
}