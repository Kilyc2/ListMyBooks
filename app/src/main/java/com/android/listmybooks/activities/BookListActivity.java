package com.android.listmybooks.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.listmybooks.R;
import com.android.listmybooks.adapters.BookRecyclerViewAdapter;
import com.android.listmybooks.data.BooksTable;
import com.android.listmybooks.models.Book;
import com.android.listmybooks.services.FetchBooksTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookListActivity extends AppCompatActivity {

    private final static String KEY_STATE_BOOKS = "kStateBooks";
    private RecyclerView recyclerView;
    private List<Book> booksInLibrary = new ArrayList<>();
    private ProgressBar spinner;
    protected boolean isTwoPane;
    private String sortOrder;
    private String displayMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        spinner = (ProgressBar) findViewById(R.id.progress_bar_library);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        isTwoPane = findViewById(R.id.book_detail_container) != null;

        recyclerView = (RecyclerView)findViewById(R.id.book_list);
        assert recyclerView != null;
        setReciclerView();
        setupRecyclerView(recyclerView);

        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_STATE_BOOKS)) {
            FetchBooksTask fetchBooksTask = new FetchBooksTask(this);
            fetchBooksTask.execute();
        } else {
            List<Book> books = savedInstanceState.getParcelableArrayList(KEY_STATE_BOOKS);
            setLibrary(books);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPreExecuteAsyncTask() {
        this.spinner.setVisibility(View.VISIBLE);
    }

    public void onPostExecuteAsyncTask(List<Book> books) {
        setLibrary(books);
        this.spinner.setVisibility(View.INVISIBLE);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void setLibrary(List<Book> books) {
        this.booksInLibrary.clear();
        for (Book book : books) {
            this.booksInLibrary.add(book);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new BookRecyclerViewAdapter(this.booksInLibrary, this.isTwoPane,
                getSupportFragmentManager()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_STATE_BOOKS,
                (ArrayList<? extends Parcelable>) this.booksInLibrary);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (setReciclerView()) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private boolean setReciclerView() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSortOrderChanged = changeSortOrder(sharedPref.getString(
                SettingsActivity.PREFERENCE_SORT_ORDER,
                BooksTable.COLUMN_TITLE));
        boolean isDisplayModeChanged = changeDisplayMode(sharedPref.getString(
                SettingsActivity.PREFERENCE_DISPLAY,
                getString(R.string.display_list)));
        return isSortOrderChanged || isDisplayModeChanged;
    }

    private boolean changeSortOrder(String sortOrder) {
        if (sortOrder.equals(this.sortOrder)) {
            return false;
        }
        this.sortOrder = sortOrder;
        boolean isOrderByTitle = sortOrder.equals(getString(R.string.sort_order_title));
        if (isOrderByTitle) {
            Collections.sort(booksInLibrary, new Comparator<Book>() {
                @Override
                public int compare(Book lhs, Book rhs) {
                    return lhs.getTitle().compareTo(rhs.getTitle());
                }
            });
        } else {
            Collections.sort(booksInLibrary, new Comparator<Book>() {
                @Override
                public int compare(Book lhs, Book rhs) {
                    return lhs.getDate().compareTo(rhs.getDate());
                }
            });
        }
        return true;
    }

    private boolean changeDisplayMode(String displayMode) {
        if (displayMode.equals(this.displayMode)) {
            return false;
        }
        this.displayMode = displayMode;
        boolean isLinearLayout = displayMode.equals(getString(R.string.display_list));
        if (isLinearLayout) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        recyclerView.setTag(isLinearLayout);
        return true;
    }
}
