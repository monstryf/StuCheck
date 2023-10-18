package com.example.stucheck;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stucheck.Model.MyCalendar;
import com.example.stucheck.Model.StudentItem;
import com.example.stucheck.Service.DataBaseHelper;
import com.example.stucheck.Service.StudentAdapter;

import java.util.ArrayList;
import java.util.Currency;

public class StudentActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String className;
    private String subjectName;
    private int position;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();
    private DataBaseHelper dataBaseHelper;
    private long cid;
    private MyCalendar calendar ;
    private TextView subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        calendar = new MyCalendar();
        dataBaseHelper = new DataBaseHelper(this);
        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");
        position = intent.getIntExtra("position",-1);
        cid = intent.getLongExtra("cid",-1);

        setToolbar();
        logData();
        recyclerView = findViewById(R.id.recyclerViewStudent);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> changeStatus(position));
        logStatus();
    }

    private void logData() {
        Cursor cursor = dataBaseHelper.getStudentTable(cid);
        Log.i("1234567890", "logData: "+cid);
        studentItems.clear();
        while (cursor.moveToNext()){
            long sid = cursor.getLong(cursor.getColumnIndexOrThrow(DataBaseHelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.STUDENT_ROLL_KEY));
            String studentName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(sid,roll,studentName));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();
        if (status.equals("P")) status = "A";
        else status = "P";
        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        subTitle = toolbar.findViewById(R.id.toolbar_subtitle);
        ImageButton back = toolbar.findViewById(R.id.back_button);
        ImageButton save = toolbar.findViewById(R.id.save_button);
        save.setOnClickListener(v->saveStatus());
        title.setText(className);
        subTitle.setText(subjectName + " | " + calendar.getDate());
        back.setOnClickListener(v -> onBackPressed());

       toolbar.inflateMenu(R.menu.student_menu);

        toolbar.setOnMenuItemClickListener(item -> onMenuItemClick(item));
    }

    private void saveStatus(){

        for (StudentItem studentItem : studentItems){
            String status = studentItem.getStatus();
         if (status!="P"){
             status = "A";
         }
          long value= dataBaseHelper.addStatus(studentItem.getSid(),cid,status,calendar.getDate());
         if (value==-1){
             dataBaseHelper.updateStatus(studentItem.getSid(),status,calendar.getDate());
         }
        }
    }
    private void logStatus(){
     for (StudentItem studentItem : studentItems){
         String status = dataBaseHelper.getStatus(studentItem.getSid(),calendar.getDate());
         if (status!=null){
             studentItem.setStatus(status);
         }
         else {
             studentItem.setStatus("");
         }
         adapter.notifyDataSetChanged();
     }
    }

    private boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.add_student){
            AddStudentDialog();
        }
        if (item.getItemId() == R.id.chang_date){
            showCalendar();
        }
        item.getIcon().setAlpha(0);
        return true;
    }

    private void showCalendar() {
        calendar.show(getSupportFragmentManager(),"");
        calendar.setOnCalenderOkClickListener(this::onCalendarOkClick);
    }
    private void onCalendarOkClick(int year,int month,int day){
       calendar.setDate(year,month,day);
        subTitle.setText(subjectName + " | " + calendar.getDate());
        logStatus();
    }

    private void AddStudentDialog() {
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(),Dialog.AddNewStudentDialog);
        dialog.setOnClickListener((roll, studentName) -> addStudent(roll,studentName));
    }

    private void addStudent(String roll_string, String studentName) {
        int roll = Integer.parseInt(roll_string);
        long sid= dataBaseHelper.insertStudent(cid,roll,studentName);
        StudentItem studentItem = new StudentItem(sid,roll,studentName);
        studentItems.add(studentItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                UpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void UpdateStudentDialog(int position) {
        Dialog dialog = new Dialog(studentItems.get(position).getRoll(),studentItems.get(position).getStudentName());
        dialog.show(getSupportFragmentManager(),Dialog.UpdateStudentDialog);
        dialog.setOnClickListener((roll, studentName) -> UpdateStudent(position,studentName));
    }

    private void UpdateStudent(int position, String studentName) {
        dataBaseHelper.UpdateStudent(studentItems.get(position).getSid(),studentName);
        studentItems.get(position).setStudentName(studentName);
        adapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        dataBaseHelper.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
        adapter.notifyItemRemoved(position);
    }
}