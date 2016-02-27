package com.android.listmybooks.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.android.listmybooks.R;
import com.android.listmybooks.services.FetchBooksTask;

import java.util.ArrayList;

/**
 * An activity representing a list of Books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BookListActivity extends BookLibraryActivity {

    private final static String KEY_STATE_BOOKS = "kStateBooks";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        spinner = (ProgressBar) findViewById(R.id.progress_bar_library);
        booksInLibrary = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_STATE_BOOKS)) {
            FetchBooksTask fetchBooksTask = new FetchBooksTask(this);
            fetchBooksTask.execute();
        } else {
            booksInLibrary = savedInstanceState.getParcelableArrayList(KEY_STATE_BOOKS);
        }

        recyclerView = findViewById(R.id.book_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.book_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_STATE_BOOKS, booksInLibrary);
    }

//    public class FetchBooksTask extends AsyncTask<Void, Void, ArrayList<Book>> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            spinner.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected ArrayList<Book> doInBackground(Void... params) {
//            ContentResolver contentResolver = getContentResolver();
//            Cursor cursor = contentResolver.query(BooksTable.getContentUri(),
//                    null, null, null, null);
//            ArrayList<Book> favoritesMovies = new ArrayList<>();
//            if(CursorHelper.isValidCursor(cursor)) {
//                do {
//                    long id = cursor.getLong(cursor.getColumnIndex(BooksTable._ID));
//                    String title = cursor.getString(
//                            cursor.getColumnIndex(BooksTable.COLUMN_TITLE));
//                    String authors = cursor.getString(
//                            cursor.getColumnIndex(BooksTable.COLUMN_AUTHORS));
//                    String coverPath = cursor.getString(
//                            cursor.getColumnIndex(BooksTable.COLUMN_COVER_PATH));
//                    String date = cursor.getString(
//                            cursor.getColumnIndex(BooksTable.COLUMN_DATE));
//                    Book movie = new Book(id, title, authors, coverPath, date);
//                    favoritesMovies.add(movie);
//                } while (cursor.moveToNext());
//            }
//            CursorHelper.closeCursor(cursor);
//            return favoritesMovies;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Book> books) {
//            booksInLibrary = books;
//            setupRecyclerView((RecyclerView) recyclerView);
//            spinner.setVisibility(View.GONE);
//        }
//    }
}
