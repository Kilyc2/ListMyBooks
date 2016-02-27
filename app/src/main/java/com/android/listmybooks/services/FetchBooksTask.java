package com.android.listmybooks.services;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;

import com.android.listmybooks.activities.BookLibraryActivity;
import com.android.listmybooks.data.BooksTable;
import com.android.listmybooks.helpers.CursorHelper;
import com.android.listmybooks.models.Book;

import java.util.ArrayList;

public class FetchBooksTask extends AsyncTask<Void, Void, ArrayList<Book>> {

    private BookLibraryActivity activity;

    public FetchBooksTask(BookLibraryActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.activity.onPreExecuteAsyncTask();
    }

    @Override
    protected void onPostExecute(ArrayList<Book> books) {
        super.onPostExecute(books);
        this.activity.onPostExecuteAsyncTask(books);
    }

    @Override
    protected ArrayList<Book> doInBackground(Void... params) {
        ContentResolver contentResolver = this.activity.getContentResolver();
        Cursor cursor = contentResolver.query(BooksTable.getContentUri(),
                null, null, null, null);
        ArrayList<Book> favoritesMovies = new ArrayList<>();
        if(CursorHelper.isValidCursor(cursor)) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(BooksTable._ID));
                String title = cursor.getString(
                        cursor.getColumnIndex(BooksTable.COLUMN_TITLE));
                String authors = cursor.getString(
                        cursor.getColumnIndex(BooksTable.COLUMN_AUTHORS));
                String coverPath = cursor.getString(
                        cursor.getColumnIndex(BooksTable.COLUMN_COVER_PATH));
                String date = cursor.getString(
                        cursor.getColumnIndex(BooksTable.COLUMN_DATE));
                Book movie = new Book(id, title, authors, coverPath, date);
                favoritesMovies.add(movie);
            } while (cursor.moveToNext());
        }
        CursorHelper.closeCursor(cursor);
        return favoritesMovies;
    }
}
