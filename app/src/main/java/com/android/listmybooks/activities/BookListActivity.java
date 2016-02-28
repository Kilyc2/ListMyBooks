package com.android.listmybooks.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.android.listmybooks.R;
import com.android.listmybooks.adapters.BookRecyclerViewAdapter;
import com.android.listmybooks.models.Book;
import com.android.listmybooks.services.FetchBooksTask;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity {

    private final static String KEY_STATE_BOOKS = "kStateBooks";
    protected View recyclerView;
    protected List<Book> booksInLibrary;
    protected boolean isTwoPane;
    protected ProgressBar spinner;

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
            isTwoPane = true;
        }
        recyclerView = findViewById(R.id.book_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    public void onPreExecuteAsyncTask() {
        this.spinner.setVisibility(View.VISIBLE);
    }

    public void onPostExecuteAsyncTask(List<Book> books) {
        this.booksInLibrary = books;
        setupRecyclerView((RecyclerView) recyclerView);
        this.spinner.setVisibility(View.INVISIBLE);
    }

    protected void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new BookRecyclerViewAdapter(this.booksInLibrary, this.isTwoPane,
                getSupportFragmentManager()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_STATE_BOOKS,
                (ArrayList<? extends Parcelable>) this.booksInLibrary);
    }
}
