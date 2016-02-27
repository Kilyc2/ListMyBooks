package com.android.listmybooks.helpers;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.android.listmybooks.data.BooksTable;

public class ListMyBooksDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "list_my_books.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CONTENT_AUTHORITY = "com.android.listmybooks.data";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public ListMyBooksDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        BooksTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        BooksTable.onUpgrade(db);
    }

    public static String getContentAuthority() {
        return CONTENT_AUTHORITY;
    }

    public static Uri getBaseContentUri() {
        return BASE_CONTENT_URI;
    }

    public static String getContentType(String cursor, String path) {
        return cursor.concat("/").concat(CONTENT_AUTHORITY).concat("/").concat(path);
    }
}
