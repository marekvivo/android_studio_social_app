package com.example.findr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class InterestDatabaseHelper extends SQLiteOpenHelper{

    private static final String INTEREST_TABLE = "INTEREST_TABLE";
    private static final String ID = "ID";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String GENRE_MAP_ID = "GENRE_MAP_ID";

    public InterestDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + INTEREST_TABLE + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GENRE_MAP_ID + " INTEGER, " + DESCRIPTION + " TEXT) ";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne (Interest interest){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(GENRE_MAP_ID, interest.toDatabaseGenreId());
        cv.put(DESCRIPTION, interest.getInterest());

        long insert = db.insert(INTEREST_TABLE, null, cv);
        if(insert == -1){
            return false;
        }
        db.close();
        return true;
    }

    //TO TEST
    //Creates a new Interest object and returns it open successful query of database
    //else returns null when not successful
    public Interest getInterest (int interestId){

        String queryStr = "SELECT * FROM " + INTEREST_TABLE + " WHERE " + ID + " = " + String.valueOf(interestId);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryStr, null);
        if(cursor.moveToFirst()){

            ArrayList<Integer> genreIdList = new ArrayList<>();

            String genreIdListStr = String.valueOf(cursor.getInt(2));

            String[] genreIdListStrArr = genreIdListStr.split("0");
            for(String s : genreIdListStrArr){
                genreIdList.add(Integer.parseInt(s));
            }

            String description = cursor.getString(1);


            db.close();
            cursor.close();
            return new Interest(genreIdList,description);

        }
        db.close();
        cursor.close();
        return null;
    }

}
