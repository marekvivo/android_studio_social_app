package com.example.findr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

public class UserDataBaseHelper extends SQLiteOpenHelper {
    private static final String USER_TABLE = "USER_TABLE";
    private static final String ID = "ID";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String EMAIL = "EMAIL";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String SALT = "SALT";


    public UserDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIRST_NAME + " TEXT, " + LAST_NAME + " TEXT, " + EMAIL + " TEXT, " + USERNAME + " TEXT, " + SALT + " TEXT, "+ PASSWORD + " TEXT )";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne (UserInfo userInfo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FIRST_NAME, userInfo.getFirstName());
        cv.put(LAST_NAME, userInfo.getLastName());
        cv.put(EMAIL, userInfo.getEmail());
        cv.put(USERNAME, userInfo.getUserName());
        cv.put(SALT, userInfo.getSalt());
        cv.put(PASSWORD, userInfo.getPassword());

        long insert = db.insert(USER_TABLE, null, cv);
        if(insert == -1){
            return false;
        }
        Log.d(userInfo.getFirstName(),userInfo.getLastName());
        db.close();
        return true;
    }

    public ArrayList<UserInfo> getAll (){
        ArrayList<UserInfo> userInfoList = new ArrayList<>();

        String queryStr = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryStr, null);

        if(cursor.moveToFirst()){
            do{
                String firstName = cursor.getString(1);
                String lastName = cursor.getString(2);
                String email = cursor.getString(3);
                String userName = cursor.getString(4);
                String salt = cursor.getString(5);
                String password = cursor.getString(6);
                Log.d("getAll:firstName", firstName);
                Log.d("ENDLOG","");
                UserInfo userInfo = new UserInfo(firstName,lastName,email,userName,salt,password);
                userInfoList.add(userInfo);
            }while (cursor.moveToNext());
        }


        cursor.close();
        db.close();
        return userInfoList;
    }

    public void deleteDb(){
        SQLiteDatabase db = this.getWritableDatabase();
        File dbFile = new File(db.getPath());
        SQLiteDatabase.deleteDatabase(dbFile);
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE,null,null);
    }
}
