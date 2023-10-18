package com.example.stucheck;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class Dialog extends DialogFragment {
    Button save;
    Button cancel;

    EditText className;
    EditText subjectName;
    EditText roll;
    EditText studentName;

    TextView title;
    public static final String AddNewClassDialog = "Add New ClassDialog";
    public static final String UpdateClassDialog = "Update ClassDialog";
    public static final String AddNewStudentDialog = "Add New StudentDialog";
    public static final String UpdateStudentDialog = "Update StudentDialog";

    private OnClickListener listener;
    private int rollStudent;
    private String studentNameString;
    private String classNameString;
    private String subjectNameString;
    public Dialog(int roll, String studentName) {
        this.rollStudent = roll;
        this.studentNameString = studentName;
    }

    public Dialog(String className, String subjectName) {
        this.classNameString = className;
        this.subjectNameString = subjectName;
    }

    public Dialog() {

    }

    public interface OnClickListener{
        void OnClick( String className, String subjectName);
    }
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        android.app.Dialog dialog = null;
        if (getTag().equals(AddNewClassDialog)){
            dialog = GetAddNewClassDialog();
        }
        if (getTag().equals(AddNewStudentDialog)){
            dialog = GetAddNewStudentDialog();
        }
        if (getTag().equals(UpdateClassDialog)){
            dialog = GetUpdateClassDialog();
        }
        if (getTag().equals(UpdateStudentDialog)){
            dialog = GetUpdateStudentDialog();
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
      return dialog;
    }

    private android.app.Dialog GetUpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        title = view.findViewById(R.id.dialog_title);
        title.setText("Update Student");

        roll = view.findViewById(R.id.d_title);
        studentName = view.findViewById(R.id.d_suptitle);
        roll.setHint(" Roll");
        studentName.setHint("Student Name");
        cancel = view.findViewById(R.id.cancel);
        roll.setText(String.valueOf(rollStudent+""));
        roll.setEnabled(false);
        studentName.setText(studentNameString);
        cancel.setOnClickListener(v-> dismiss());
        save = view.findViewById(R.id.save);
        save.setOnClickListener(v->{
            String rolls = roll.getText().toString();
            String student_Name = studentName.getText().toString();
            listener.OnClick(rolls,student_Name);
            dismiss();
        });

        return builder.create();
    }

    private android.app.Dialog GetUpdateClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        title = view.findViewById(R.id.dialog_title);
        title.setText("Update Class");

        className = view.findViewById(R.id.d_title);
        subjectName = view.findViewById(R.id.d_suptitle);
        className.setHint("Class Name");
        subjectName.setHint("Subject Name");
        className.setText(classNameString);
        subjectName.setText(subjectNameString);
        save = view.findViewById(R.id.save);
        save.setOnClickListener(v->{
            String nameClass = className.getText().toString();
            String nameSubject = subjectName.getText().toString();
            listener.OnClick( nameClass, nameSubject);
            dismiss();
        });
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(v-> dismiss());
        return builder.create();
    }

    private android.app.Dialog GetAddNewStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        title = view.findViewById(R.id.dialog_title);
        title.setText("Add New Student");

        roll = view.findViewById(R.id.d_title);
        studentName = view.findViewById(R.id.d_suptitle);
        roll.setHint(" Roll");
        studentName.setHint("Student Name");
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(v-> dismiss());
        save = view.findViewById(R.id.save);
        save.setOnClickListener(v->{
            String rolls = roll.getText().toString();
            String student_Name = studentName.getText().toString();
            roll.setText(String.valueOf(Integer.parseInt(rolls)+1));
            studentName.setText("");
            listener.OnClick(rolls,student_Name);

        });

        return builder.create();
    }

    private android.app.Dialog GetAddNewClassDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        title = view.findViewById(R.id.dialog_title);
        title.setText("Add New Class");

        className = view.findViewById(R.id.d_title);
        subjectName = view.findViewById(R.id.d_suptitle);
        className.setHint("Class Name");
        subjectName.setHint("Subject Name");
        save = view.findViewById(R.id.save);
        save.setOnClickListener(v->{
            String nameClass = className.getText().toString();
            String nameSubject = subjectName.getText().toString();
            listener.OnClick( nameClass, nameSubject);
            dismiss();
        });
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(v-> dismiss());
        return builder.create();
    }
}
