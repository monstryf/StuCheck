package com.example.stucheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.stucheck.Model.ClassItem;
import com.example.stucheck.Service.ClassAdapter;
import com.example.stucheck.Service.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton floatActionButton;
    private Toolbar toolbar;
    private RecyclerView recyclerView ;
    private ClassAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ClassItem> classItem = new ArrayList<>();
    private DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseHelper = new DataBaseHelper(this);
        floatActionButton = findViewById(R.id.add_class);
        floatActionButton.setOnClickListener(v -> AddClassDialog());
        logData();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ClassAdapter(this,classItem);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> gotoItemActivity(position));
      setToolbar();
    }

    private void logData() {
        Cursor cursor = dataBaseHelper.getClassTable();
        classItem.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.C_ID));
            String className = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.CLASS_NAME_KEY));
            String subjectName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.SUBJECT_NAME_KEY));
            classItem.add(new ClassItem(id,className,subjectName));
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        TextView subTitle = toolbar.findViewById(R.id.toolbar_subtitle);
        ImageButton back = toolbar.findViewById(R.id.back_button);
        ImageButton save = toolbar.findViewById(R.id.save_button);
        title.setText("StuCheck");
        subTitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this,StudentActivity.class);
        intent.putExtra("className",classItem.get(position).getClassName());
        intent.putExtra("subjectName",classItem.get(position).getSubjectName());
        intent.putExtra("position",position);
        intent.putExtra("cid",classItem.get(position).getCid());
        startActivity(intent);
    }

    private void AddClassDialog(){
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(),Dialog.AddNewClassDialog);
        dialog.setOnClickListener((className, subjectName) -> AddClass(className,subjectName));
    }
    private void AddClass(String className, String subjectName) {
        long cid = dataBaseHelper.insertClass(className,subjectName);
        ClassItem classItems = new ClassItem(cid,className,subjectName);
        classItem.add(classItems);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                UpdateClassDialog(item.getGroupId());
                break;
            case 1:
           deleteVlass(item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void UpdateClassDialog(int position) {
        Dialog dialog = new Dialog(classItem.get(position).getClassName(),classItem.get(position).getSubjectName());
        dialog.show(getSupportFragmentManager(),Dialog.UpdateClassDialog);
        dialog.setOnClickListener((className, subjectName) -> UpdateClass(position,className,subjectName));
    }

    private void UpdateClass(int position, String className, String subjectName) {
        dataBaseHelper.UpdateClass(classItem.get(position).getCid(),className,subjectName);
        classItem.get(position).setClassName(className);
        classItem.get(position).setSubjectName(subjectName);
        adapter.notifyItemChanged(position);
    }

    private void deleteVlass(int position) {
        dataBaseHelper.deleteClass(classItem.get(position).getCid());
        classItem.remove(position);
        adapter.notifyItemRemoved(position);
    }
}