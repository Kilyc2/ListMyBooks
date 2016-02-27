package com.android.listmybooks.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.listmybooks.R;
import com.android.listmybooks.activities.BookDetailActivity;
import com.android.listmybooks.activities.BookListActivity;
import com.android.listmybooks.models.Book;

/**
 * A fragment representing a single Book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {
    /**
     * The fragment argument representing the book ID that this fragment
     * represents.
     */
    public static final String ARG_BOOK = "aBook";

    /**
     * The book content this fragment is presenting.
     */
    private Book bookItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_BOOK)) {
            // Load the book content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            bookItem = getArguments().getParcelable(ARG_BOOK);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =
                    (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(bookItem.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_detail, container, false);

        if (bookItem != null) {
            ((TextView) rootView.findViewById(R.id.book_detail)).setText(bookItem.getTitle());
        }

        return rootView;
    }
}
