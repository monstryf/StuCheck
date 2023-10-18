package com.example.stucheck.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    // CLASS TABLE
    private static final String CLASS_TABLE_NAME = "CLASS_TABLE";
    public static final String C_ID = "_CID";
    public static final String CLASS_NAME_KEY = "CLASS_NAME";
    public static final String SUBJECT_NAME_KEY = "SUBJECT_NAME";
    private static final String CREATE_CLASS_TABLE =
            "CREATE TABLE " + CLASS_TABLE_NAME + "(" +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + CLASS_NAME_KEY + " TEXT NOT NULL, "
                    + SUBJECT_NAME_KEY + " TEXT NOT NULL,"+
        "UNIQUE("+CLASS_NAME_KEY+","+SUBJECT_NAME_KEY+")"+");";
    private static final String DROP_CLASS_TABLE = "DROP TABLE IF EXISTS " + CLASS_TABLE_NAME;
    private static final String SELECT_ALL_CLASS_TABLE = "SELECT * FROM " + CLASS_TABLE_NAME;
    // STUDENT TABLE
    private static final String STUDENT_TABLE_NAME = "STUDENT_TABLE";
    public static final String S_ID = "_SID";
    public static final String STUDENT_NAME_KEY = "STUDENT_NAME";
    public static final String STUDENT_ROLL_KEY = "ROLL";
    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + STUDENT_TABLE_NAME + "(" +
                     S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + C_ID + " INTEGER NOT NULL, "
                    + STUDENT_NAME_KEY + " TEXT NOT NULL, "
                    + STUDENT_ROLL_KEY + " INTEGER , "
                    + "FOREIGN KEY (" + C_ID + ") REFERENCES " + CLASS_TABLE_NAME + "(" + C_ID + ")" + ");";
    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME;
    private static final String SELECT_ALL_STUDENT_TABLE = "SELECT * FROM " + STUDENT_TABLE_NAME;
    // STATUS TABLE
    private static final String STATUS_TABLE_NAME = "STATUS_TABLE";
    public static final String STATUS_ID = "_STATUS_ID";
    public static final String STATUS_KEY = "STATUS";
    public static final String DATE_KEY = "DATE";
    private static final String CREATE_STATUS_TABLE =
            "CREATE TABLE " + STATUS_TABLE_NAME + "(" +
                    STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + S_ID + " INTEGER NOT NULL, "
                    + STATUS_KEY + " TEXT NOT NULL, "
                    + DATE_KEY + " DATE NOT NULL, "+
                    "UNIQUE("+S_ID+","+DATE_KEY+")"+","
                    + "FOREIGN KEY (" + S_ID + ") REFERENCES " + STUDENT_TABLE_NAME + "(" + S_ID + ")" + ");";
    private static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS " + STATUS_TABLE_NAME;
    private static final String SELECT_ALL_STATUS_TABLE = "SELECT * FROM " + STATUS_TABLE_NAME;
    public DataBaseHelper(@Nullable Context context) {
        super(context, "StuCheck.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_CLASS_TABLE);
            sqLiteDatabase.execSQL(CREATE_STUDENT_TABLE);
            sqLiteDatabase.execSQL(CREATE_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            sqLiteDatabase.execSQL(DROP_CLASS_TABLE);
            sqLiteDatabase.execSQL(DROP_STUDENT_TABLE);
            sqLiteDatabase.execSQL(DROP_STATUS_TABLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // Class Handler
   public long insertClass(String className,String subjectName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME_KEY,className);
        values.put(SUBJECT_NAME_KEY,subjectName);
        return db.insert(CLASS_TABLE_NAME,null,values);
    }
   public Cursor getClassTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(SELECT_ALL_CLASS_TABLE,null);
    }
    public long UpdateClass(long cid,String className,String subjectName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME_KEY,className);
        values.put(SUBJECT_NAME_KEY,subjectName);
        return db.update(CLASS_TABLE_NAME,values,C_ID+"=?",new String[]{String.valueOf(cid)});
    }
   public int deleteClass(long cid){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CLASS_TABLE_NAME,C_ID+"=?",new String[]{String.valueOf(cid)});
    }
    // Student Handler
    public long insertStudent(long cid,int roll,String studentName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_ID,cid);
        values.put(STUDENT_ROLL_KEY,roll);
        values.put(STUDENT_NAME_KEY,studentName);
        return db.insert(STUDENT_TABLE_NAME,null,values);
    }
    public Cursor getStudentTable(long cid){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(STUDENT_TABLE_NAME,null,C_ID+"=?",new String[]{String.valueOf(cid)},null,null,STUDENT_ROLL_KEY);
    }
    public long UpdateStudent(long sid,String studentName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME_KEY,studentName);
        return db.update(STUDENT_TABLE_NAME,values,S_ID+"=?",new String[]{String.valueOf(sid)});
    }
    public int deleteStudent(long sid){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(STUDENT_TABLE_NAME,S_ID+"=?",new String[]{String.valueOf(sid)});
    }
    public long addStatus(long sid,String status,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(S_ID,sid);
        values.put(STATUS_KEY,status);
        values.put(DATE_KEY,date);
        return db.insert(STATUS_TABLE_NAME,null,values);
    }
    public long updateStatus(long sid,String status,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_KEY,status);
        String whereClause = DATE_KEY+"='"+date+"'And "+S_ID+"="+sid;
        return db.update(STATUS_TABLE_NAME,values,whereClause,null);
    }
    public String getStatus(long sid,String date){
        String status = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = DATE_KEY+"='"+date+"'And "+S_ID+"="+sid;
        Cursor cursor = db.query(STATUS_TABLE_NAME,null,whereClause,null,null,null,null);
        if (cursor.moveToNext()){
            status = cursor.getString(cursor.getColumnIndexOrThrow(STATUS_KEY));
        }
        return status;
    }
}
