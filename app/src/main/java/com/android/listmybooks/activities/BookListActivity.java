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

        if (findViewById(R.id.book_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true;
        }

        recyclerView = findViewById(R.id.book_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_STATE_BOOKS, booksInLibrary);
    }
}
