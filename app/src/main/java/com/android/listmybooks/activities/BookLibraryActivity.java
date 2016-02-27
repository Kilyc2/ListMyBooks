package com.android.listmybooks.activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.listmybooks.adapters.BookRecyclerViewAdapter;
import com.android.listmybooks.models.Book;

import java.util.ArrayList;

public abstract class BookLibraryActivity extends AppCompatWithSpinnerActivity {

    protected View recyclerView;
    protected ArrayList<Book> booksInLibrary;
    protected boolean isTwoPane;

    public void onPreExecuteAsyncTask() {
        setSpinnerVisible();
    }

    public void onPostExecuteAsyncTask(ArrayList<Book> books) {
        booksInLibrary = books;
        setupRecyclerView((RecyclerView) recyclerView);
        setSpinnerInvisible();
    }

    protected void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new BookRecyclerViewAdapter(this.booksInLibrary, this.isTwoPane,
                getSupportFragmentManager()));
    }
}
