package com.example.quangtruong3851050176.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quangtruong3851050176.models.CompletedTodoModel;
import com.example.quangtruong3851050176.models.PendingTodoModel;

import java.util.ArrayList;


public class TodoDBHelper {
    private Context context;
    private DatabaseHelper databaseHelper;

    public TodoDBHelper(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    //them cong viec
    public boolean addNewTodo(PendingTodoModel pendingTodoModel) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_TODO_TITLE, pendingTodoModel.getTodoTitle());
        contentValues.put(DatabaseHelper.COL_TODO_CONTENT, pendingTodoModel.getTodoContent());
        contentValues.put(DatabaseHelper.COL_TODO_TAG, pendingTodoModel.getTodoTag());
        contentValues.put(DatabaseHelper.COL_TODO_DATE, pendingTodoModel.getTodoDate());
        contentValues.put(DatabaseHelper.COL_TODO_TIME, pendingTodoModel.getTodoTime());
        contentValues.put(DatabaseHelper.COL_TODO_STATUS, DatabaseHelper.COL_DEFAULT_STATUS);
        sqLiteDatabase.insert(DatabaseHelper.TABLE_TODO_NAME, null, contentValues);
        sqLiteDatabase.close();
        return true;
    }

    //dem so cong viec
    public int countTodos() {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        String count = "SELECT " + DatabaseHelper.COL_TODO_ID + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_STATUS + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(count, new String[]{DatabaseHelper.COL_DEFAULT_STATUS});
        return cursor.getCount();
    }

    //dem so cong viec da hoan thnah
    public int countCompletedTodos() {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        String count = "SELECT " + DatabaseHelper.COL_TODO_ID + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_STATUS + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(count, new String[]{DatabaseHelper.COL_STATUS_COMPLETED});
        return cursor.getCount();
    }

    //lay danh sach cong viec
    public ArrayList<PendingTodoModel> fetchAllTodos() {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        ArrayList<PendingTodoModel> pendingTodoModels = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_TODO_NAME + " INNER JOIN " + DatabaseHelper.TABLE_TAG_NAME + " ON " + DatabaseHelper.TABLE_TODO_NAME + "." + DatabaseHelper.COL_TODO_TAG + "=" +
                DatabaseHelper.TABLE_TAG_NAME + "." + DatabaseHelper.COL_TAG_ID + " WHERE " + DatabaseHelper.COL_TODO_STATUS + "=? ORDER BY " + DatabaseHelper.TABLE_TODO_NAME + "." + DatabaseHelper.COL_TODO_ID + " ASC";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{DatabaseHelper.COL_DEFAULT_STATUS});
        while (cursor.moveToNext()) {
            PendingTodoModel pendingTodoModel = new PendingTodoModel();
            pendingTodoModel.setTodoID(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TODO_ID)));
            pendingTodoModel.setTodoTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TITLE)));
            pendingTodoModel.setTodoContent(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_CONTENT)));
            pendingTodoModel.setTodoTag(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TAG_TITLE)));
            pendingTodoModel.setTodoDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_DATE)));
            pendingTodoModel.setTodoTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TIME)));
            pendingTodoModels.add(pendingTodoModel);
        }
        cursor.close();
        sqLiteDatabase.close();
        return pendingTodoModels;
    }

    //lay danh sach cong viec da hoan thanh
    public ArrayList<CompletedTodoModel> fetchCompletedTodos() {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        ArrayList<CompletedTodoModel> completedTodoModels = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_TODO_NAME + " INNER JOIN " + DatabaseHelper.TABLE_TAG_NAME + " ON " + DatabaseHelper.TABLE_TODO_NAME + "." + DatabaseHelper.COL_TODO_TAG + "=" +
                DatabaseHelper.TABLE_TAG_NAME + "." + DatabaseHelper.COL_TAG_ID + " WHERE " + DatabaseHelper.COL_TODO_STATUS + "=? ORDER BY " + DatabaseHelper.TABLE_TODO_NAME + "." + DatabaseHelper.COL_TODO_ID + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{DatabaseHelper.COL_STATUS_COMPLETED});
        while (cursor.moveToNext()) {
            CompletedTodoModel completedTodoModel = new CompletedTodoModel();
            completedTodoModel.setTodoID(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TODO_ID)));
            completedTodoModel.setTodoTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TITLE)));
            completedTodoModel.setTodoContent(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_CONTENT)));
            completedTodoModel.setTodoTag(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TAG_TITLE)));
            completedTodoModel.setTodoDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_DATE)));
            completedTodoModel.setTodoTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TIME)));
            completedTodoModels.add(completedTodoModel);
        }
        cursor.close();
        sqLiteDatabase.close();
        return completedTodoModels;
    }

    //cap nhat cong viec
    public boolean updateTodo(PendingTodoModel pendingTodoModel) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_TODO_TITLE, pendingTodoModel.getTodoTitle());
        contentValues.put(DatabaseHelper.COL_TODO_CONTENT, pendingTodoModel.getTodoContent());
        contentValues.put(DatabaseHelper.COL_TODO_TAG, pendingTodoModel.getTodoTag());
        contentValues.put(DatabaseHelper.COL_TODO_DATE, pendingTodoModel.getTodoDate());
        contentValues.put(DatabaseHelper.COL_TODO_TIME, pendingTodoModel.getTodoTime());
        contentValues.put(DatabaseHelper.COL_TODO_STATUS, DatabaseHelper.COL_DEFAULT_STATUS);
        sqLiteDatabase.update(DatabaseHelper.TABLE_TODO_NAME, contentValues, DatabaseHelper.COL_TODO_ID + "=?", new String[]{String.valueOf(pendingTodoModel.getTodoID())});
        sqLiteDatabase.close();
        return true;
    }

    //hoan thanh cong viec theo id
    public boolean makeCompleted(int todoID) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_TODO_STATUS, DatabaseHelper.COL_STATUS_COMPLETED);
        sqLiteDatabase.update(DatabaseHelper.TABLE_TODO_NAME, contentValues, DatabaseHelper.COL_TODO_ID + "=?",
                new String[]{String.valueOf(todoID)});
        sqLiteDatabase.close();
        return true;
    }

    //xoa cong viec theo id
    public boolean removeTodo(int todoID) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        sqLiteDatabase.delete(DatabaseHelper.TABLE_TODO_NAME, DatabaseHelper.COL_TODO_ID + "=?", new String[]{String.valueOf(todoID)});
        sqLiteDatabase.close();
        return true;
    }

    //xoa tat ca cong viec
    public boolean removeCompletedTodos() {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        sqLiteDatabase.delete(DatabaseHelper.TABLE_TODO_NAME, DatabaseHelper.COL_TODO_STATUS + "=?", new String[]{DatabaseHelper.COL_STATUS_COMPLETED});
        sqLiteDatabase.close();
        return true;
    }

    //tim cong viec theo ten
    public String fetchTodoTitle(int todoID) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_TODO_TITLE + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_ID + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(todoID)});
        String title = "";
        if (cursor.moveToFirst()) {
            title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TITLE));
        }
        cursor.close();
        sqLiteDatabase.close();
        return title;
    }

    //tim cong viec theo id
    public String fetchTodoContent(int todoID) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_TODO_CONTENT + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_ID + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(todoID)});
        String content = "";
        if (cursor.moveToFirst()) {
            content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_CONTENT));
        }
        cursor.close();
        sqLiteDatabase.close();
        return content;
    }

    //tim thoi gian cong viec theo id
    public String fetchTodoDate(int todoID) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_TODO_DATE + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_ID + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(todoID)});
        String date = "";
        if (cursor.moveToFirst()) {
            date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_DATE));
        }
        cursor.close();
        sqLiteDatabase.close();
        return date;
    }

    //tim thoi gian cong viec theo id
    public String fetchTodoTime(int todoID) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_TODO_TIME + " FROM " + DatabaseHelper.TABLE_TODO_NAME + " WHERE " + DatabaseHelper.COL_TODO_ID + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(todoID)});
        String time = "";
        if (cursor.moveToFirst()) {
            time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TODO_TIME));
        }
        cursor.close();
        sqLiteDatabase.close();
        return time;
    }
}
