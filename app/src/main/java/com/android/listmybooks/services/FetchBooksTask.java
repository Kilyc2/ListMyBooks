package com.android.listmybooks.services;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;

import com.android.listmybooks.activities.BookListActivity;
import com.android.listmybooks.data.BooksTable;
import com.android.listmybooks.helpers.CursorHelper;
import com.android.listmybooks.models.Book;

import java.util.ArrayList;
import java.util.List;

public class FetchBooksTask extends AsyncTask<Void, Void, List<Book>> {

    private BookListActivity activity;

    public FetchBooksTask(BookListActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.activity.onPreExecuteAsyncTask();
    }

    @Override
    protected void onPostExecute(List<Book> books) {
        super.onPostExecute(books);
        this.activity.onPostExecuteAsyncTask(books);
    }

    @Override
    protected List<Book> doInBackground(Void... params) {
        ContentResolver contentResolver = this.activity.getContentResolver();
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
