package com.snake.common.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ClassSQLite {

    private Context mContext;

    public ClassSQLite(Context context) {
        this.mContext = context;
    }

    public SQLiteDatabase openDataBaseByName(String dbName) {
        SQLiteDatabase db;
        db = mContext.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        return db;
    }

    public void createDataBaseByName (String dbName) {
        SQLiteDatabase db = mContext.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        db.close();
    }

    public boolean deleteDataBaseByName (String dbName) {
        return mContext.deleteDatabase(dbName);
    }
}
