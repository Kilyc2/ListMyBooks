package com.android.listmybooks.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.android.listmybooks.helpers.ListMyBooksDBHelper;

public class BooksProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ListMyBooksDBHelper dbHelper;

    public static final int BOOK = 100;
    public static final int BOOK_ID = 101;

    @Override
    public boolean onCreate() {
        dbHelper = new ListMyBooksDBHelper(getContext());
        return true;
    }

    private Cursor getBooks(String sortOrder) {
        SQLiteQueryBuilder booksQueryBuilder = new SQLiteQueryBuilder();
        booksQueryBuilder.setTables(BooksTable.TABLE_NAME);

        return booksQueryBuilder.query(dbHelper.getReadableDatabase(), null,
                null, null, null, null, sortOrder);
    }

    private Cursor getBook(Uri uri) {
        SQLiteQueryBuilder bookQueryBuilder = new SQLiteQueryBuilder();
        bookQueryBuilder.setTables(BooksTable.TABLE_NAME);
        String selectedBook = BooksTable._ID + " = ?";
        String _id = uri.getLastPathSegment();
        String[] selectionArgs = new String[]{_id};
        return bookQueryBuilder.query(dbHelper.getReadableDatabase(), null,
                selectedBook, selectionArgs, null, null, null);
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ListMyBooksDBHelper.getContentAuthority();
        matcher.addURI(authority, BooksTable.getPathBooksTable(), BOOK);
        matcher.addURI(authority, BooksTable.getPathBooksTable().concat("/#"), BOOK_ID);
        return matcher;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOK_ID:
                return BooksTable.getContentItemType();
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        switch (sUriMatcher.match(uri)) {
            case BOOK: {
                returnCursor = getBooks(sortOrder);
                break;
            }
            case BOOK_ID: {
                returnCursor = getBook(uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case BOOK: {
                long _id = db.insert(BooksTable.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = BooksTable.buildBookUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String idBook = uri.getLastPathSegment();
        String[] whereArgs = new String[]{idBook};
        int rowsDeleted;
        switch (match) {
            case BOOK_ID: {
                rowsDeleted = db.delete(BooksTable.TABLE_NAME,
                        BooksTable._ID + "= ?", whereArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted > 0) {
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }
}
