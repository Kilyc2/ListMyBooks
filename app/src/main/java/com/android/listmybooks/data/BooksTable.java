package com.android.listmybooks.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.android.listmybooks.helpers.ListMyBooksDBHelper;
import com.android.listmybooks.models.Book;

public class BooksTable implements BaseColumns {

    public static final String TABLE_NAME = "books";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHORS = "authors";
    public static final String COLUMN_COVER_PATH = "cover_path";
    public static final String COLUMN_DATE = "date";

    private static final String PATH_BOOKS = "books";

    private static final Uri CONTENT_URI =
            ListMyBooksDBHelper.getBaseContentUri().buildUpon().appendPath(PATH_BOOKS).build();

    private static final String CONTENT_TYPE = ListMyBooksDBHelper
            .getContentType(ContentResolver.CURSOR_DIR_BASE_TYPE, PATH_BOOKS);

    private static final String CONTENT_ITEM_TYPE = ListMyBooksDBHelper
            .getContentType(ContentResolver.CURSOR_ITEM_BASE_TYPE, PATH_BOOKS);

    private static final String CREATE_TABLE = "create table " +
            TABLE_NAME + "(" +
            _ID + " integer primary key autoincrement, " +
            COLUMN_TITLE + " text not null, " +
            COLUMN_AUTHORS + " text not null, " +
            COLUMN_COVER_PATH + " text not null, " +
            COLUMN_DATE + " text not null" +
            ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public static Uri getContentUri() {
        return CONTENT_URI;
    }

    public static Uri buildBookUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static String getPathBooksTable() {
        return PATH_BOOKS;
    }

    public static String getContentType() {
        return CONTENT_TYPE;
    }

    public static String getContentItemType() {
        return CONTENT_ITEM_TYPE;
    }

    public static ContentValues getValuesBook(Book book) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHORS, book.getAuthorsForDb());
        values.put(COLUMN_COVER_PATH, book.getCoverPath());
        values.put(COLUMN_DATE, book.getDate());
        return values;
    }
}
