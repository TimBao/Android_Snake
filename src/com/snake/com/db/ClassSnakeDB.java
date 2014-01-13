package com.snake.com.db;

import com.snake.common.data.ClassSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ClassSnakeDB {

    private ClassSQLite dbManager;

    protected static final String DATABASE_NAME = "snake.db";
    protected static final String TABLE_NAME = "userinfo";

    public ClassSnakeDB(Context context) {
        dbManager = new ClassSQLite(context);
        if (dbManager != null) {
            initDB();
        }
    }

    private void initDB() {
        //create or open database
        SQLiteDatabase db = dbManager.openDataBaseByName(DATABASE_NAME);
        createTableByName(db, TABLE_NAME);

    }

    public void closeDB() {
        if (dbManager != null) {
            dbManager.openDataBaseByName(DATABASE_NAME).close();
        }
    }

    @SuppressWarnings("finally")
    public boolean createTableByName(SQLiteDatabase db, String TableName) {

        String sqlString = "CREATE TABLE " + TableName + " (name TEXT, score INTEGER);";

        try {
            db.execSQL(sqlString);

        } catch (Exception e) {

            return false;
        } finally {
            return true;
        }
    }

    @SuppressWarnings("finally")
    public boolean addUserInfo(String name, int score) {
        ContentValues values = new ContentValues();
        try {
            values = new ContentValues();
            values.put("name", name);
            values.put("score", score);
            SQLiteDatabase db = dbManager.openDataBaseByName(DATABASE_NAME);
            db.insert(TABLE_NAME, null, values);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            return true;
        }
    }

    public Cursor getCursorByTableName() {
        if (dbManager != null) {
            SQLiteDatabase db = dbManager.openDataBaseByName(DATABASE_NAME);
            String sql = "select * from " + TABLE_NAME + " order by score DESC;";
            Cursor cursor = db.rawQuery(sql, null);
            //@TODO: db.close() makes cursor.getCount() crash??????
            if (cursor.getCount() == 0)
                return null;
            else
                return cursor;
        }
        return null;
    }

    public String[][] getUserInfo(){
        Cursor cursor = getCursorByTableName();
        if (cursor == null) {
            return null;
        }
        int mycount = cursor.getCount();
        if (mycount == 0)
            return null;
        String[][] userInfo = new String[mycount][2];
        cursor.moveToFirst();
        int i = 0;
        for (i = 0; i < mycount; i++) {
            userInfo[i][0] = cursor.getString(cursor.getColumnIndex("name"));
            userInfo[i][1] = cursor.getString(cursor.getColumnIndex("score"));
            cursor.moveToNext();
        }
        cursor.close();
        return userInfo;
    }
}
