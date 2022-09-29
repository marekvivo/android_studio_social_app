package com.example.findr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UserInterestDatabaseHelper extends SQLiteOpenHelper {

    private static final String USER_INTEREST_TABLE = "USER_INTEREST_TABLE";
    private static final String ID = "ID";
    private static final String USER_ID = "USER_ID";
    private static final String INTEREST_ID = "INTEREST_ID";

    public UserInterestDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + USER_INTEREST_TABLE + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_ID + " INTEGER, " + INTEREST_ID + " INTEGER )";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne (int userId, int interestId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, userId);
        cv.put(INTEREST_ID,interestId);

        long insert = db.insert(USER_INTEREST_TABLE, null, cv);
        if(insert == -1){
            return false;
        }
        db.close();
        return true;
    }

    //TO TEST
    //Returns list of interest ids related to the user in question
    public ArrayList<Integer> getUserInterestIdList (int userId){
        ArrayList<Integer> userInterestIdList = new ArrayList<>();

        String queryStr = "SELECT "+ INTEREST_ID + " FROM " + USER_INTEREST_TABLE + " WHERE " + USER_ID + " = " + String.valueOf(userId);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryStr, null);

        if(cursor.moveToFirst()){
            do {
                userInterestIdList.add(cursor.getInt(0));
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userInterestIdList;
    }


}
