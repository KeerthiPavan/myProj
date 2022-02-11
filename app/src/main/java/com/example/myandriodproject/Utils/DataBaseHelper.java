package com.example.myandriodproject.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myandriodproject.model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static  final String DATABASE_NAME = "TODO_DATABASE";
    private static  final String TABLE_NAME = "TODO_TABLE";
    private static  final String COL_1 = "ID";
    private static  final String COL_2 = "TASK_TITLE";
    private static  final String COL_3 = "TASK_DESCRIPTION";
    private static  final String COL_4 = "TASK_DATE";
    private static  final String COL_5 = "TASK_TIME";
    private static  final String COL_6 = "TASK_STATUS";


    public DataBaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK_TITLE TEXT , TASK_DESCRIPTION TEXT, TASK_DATE TEXT, TASK_TIME TEXT, TASK_STATUS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void insertTask (ToDoModel model){
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, model.getTaskTitle());
        values.put(COL_3, model.getTaskDescription());
        values.put(COL_4, model.getDate());
        values.put(COL_5, model.getTime());
        values.put(COL_6, model.getStatus());
        db.insert(TABLE_NAME, null, values);
    }
    public void updateTask(int id , String taskTitle, String taskDescription, String date, String time ){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, taskTitle);
        values.put(COL_3, taskDescription);
        values.put(COL_4, date);
        values.put(COL_5, time);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }
    public void updateStatus(int id , int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_6 , status);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id ){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME , "ID=?" , new String[]{String.valueOf(id)});
    }
    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME , null , null , null , null , null , null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTaskTitle(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setTaskDescription(cursor.getString(cursor.getColumnIndex(COL_3)));
                        task.setDate(cursor.getString(cursor.getColumnIndex(COL_4)));
                        task.setTime(cursor.getString(cursor.getColumnIndex(COL_5)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_6)));
                        modelList.add(task);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

}
