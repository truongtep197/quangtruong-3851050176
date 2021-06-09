package com.example.quangtruong3851050176.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.quangtruong3851050176.models.TagsModel;

import java.util.ArrayList;

public class TagDBHelper {
    private Context context;
    private DatabaseHelper databaseHelper;

    public TagDBHelper(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    //them tag moi vao db
    public boolean addNewTag(TagsModel tagsModel) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_TAG_TITLE, tagsModel.getTagTitle());
        sqLiteDatabase.insert(DatabaseHelper.TABLE_TAG_NAME, null, contentValues);
        sqLiteDatabase.close();
        return true;
    }

    //kiem tra tag ton tai chua
    public boolean tagExists(String tagTitle) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_TAG_TITLE + " FROM " +
                DatabaseHelper.TABLE_TAG_NAME + " WHERE " + DatabaseHelper.COL_TAG_TITLE + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{tagTitle});
        return (cursor.getCount() > 0) ? true : false;
    }

    //dem so tag
    public int countTags() {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_TAG_ID + " FROM " + DatabaseHelper.TABLE_TAG_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        return cursor.getCount();
    }

    //get tag tu db
    public ArrayList<TagsModel> fetchTags() {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        ArrayList<TagsModel> tagsModels = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_TAG_NAME + " ORDER BY " + DatabaseHelper.COL_TAG_ID + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        while (cursor.moveToNext()) {
            TagsModel tagsModel = new TagsModel();
            tagsModel.setTagID(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TAG_ID)));
            tagsModel.setTagTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TAG_TITLE)));
            tagsModels.add(tagsModel);
        }
        cursor.close();
        sqLiteDatabase.close();
        return tagsModels;
    }

    //xoa tag bang id
    public boolean removeTag(int tagID) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        sqLiteDatabase.execSQL(DatabaseHelper.FORCE_FOREIGN_KEY);
        sqLiteDatabase.delete(DatabaseHelper.TABLE_TAG_NAME, DatabaseHelper.COL_TAG_ID + "=?",
                new String[]{String.valueOf(tagID)});
        sqLiteDatabase.close();
        return true;
    }

    //cap nhat tag
    public boolean saveTag(TagsModel tagsModel) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_TAG_TITLE, tagsModel.getTagTitle());
        sqLiteDatabase.update(DatabaseHelper.TABLE_TAG_NAME, contentValues, DatabaseHelper.COL_TAG_ID + "=?",
                new String[]{String.valueOf(tagsModel.getTagID())});
        sqLiteDatabase.close();
        return true;
    }


    //get tat ca tieu de cua tag
    public ArrayList<String> fetchTagStrings() {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        ArrayList<String> tagsModels = new ArrayList<>();
        String query = "SELECT " + DatabaseHelper.COL_TAG_TITLE + " FROM " + DatabaseHelper.TABLE_TAG_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        while (cursor.moveToNext()) {
            tagsModels.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TAG_TITLE)));
        }
        cursor.close();
        sqLiteDatabase.close();
        return tagsModels;
    }

    //tim tag bang id
    public String fetchTagTitle(int tagID) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        String fetchTitle = "SELECT " + DatabaseHelper.COL_TAG_TITLE + " FROM " + DatabaseHelper.TABLE_TAG_NAME
                + " WHERE " + DatabaseHelper.COL_TAG_ID + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(fetchTitle, new String[]{String.valueOf(tagID)});
        String title = "";
        if (cursor.moveToFirst()) {
            title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TAG_TITLE));
        }
        cursor.close();
        sqLiteDatabase.close();
        return title;
    }

    //tim tag bang ten
    public int fetchTagID(String tagTitle) {
        SQLiteDatabase sqLiteDatabase = this.databaseHelper.getReadableDatabase();
        String fetchTitle = "SELECT " + DatabaseHelper.COL_TAG_ID + " FROM " + DatabaseHelper.TABLE_TAG_NAME
                + " WHERE " + DatabaseHelper.COL_TAG_TITLE + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(fetchTitle, new String[]{tagTitle});
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TAG_ID));
    }
}
