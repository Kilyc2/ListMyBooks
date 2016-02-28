package com.android.listmybooks.services;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;

import com.android.listmybooks.activities.BookListActivity;
import com.android.listmybooks.data.BooksTable;
import com.android.listmybooks.helpers.CursorHelper;
import com.android.listmybooks.models.Book;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FetchBooksTask extends AsyncTask<Void, Void, List<Book>> {

    WeakReference<Activity> weakActivity;

    public FetchBooksTask(BookListActivity activity) {
        this.weakActivity = new WeakReference<Activity>(activity);
    }

    @Override
    protected void onPreExecute() {
        Activity activity = this.weakActivity.get();
        if (activity != null) {
            super.onPreExecute();
            ((BookListActivity)activity).onPreExecuteAsyncTask();
        }
    }

    @Override
    protected void onPostExecute(List<Book> books) {
        Activity activity = this.weakActivity.get();
        if (activity != null) {
            super.onPostExecute(books);
            ((BookListActivity)activity).onPostExecuteAsyncTask(books);
        }
    }

    @Override
    protected List<Book> doInBackground(Void... params) {
        ContentResolver contentResolver = this.weakActivity.get().getContentResolver();
        Cursor cursor = contentResolver.query(BooksTable.getContentUri(),
                null, null, null, null);
        List<Book> books = new ArrayList<>();
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
                Book book = new Book(id, title, authors, coverPath, date);
                books.add(book);
            } while (cursor.moveToNext());
        }
        CursorHelper.closeCursor(cursor);
        return books;
    }
}
