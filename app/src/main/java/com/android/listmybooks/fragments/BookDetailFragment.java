package com.android.listmybooks.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.listmybooks.R;
import com.android.listmybooks.models.Book;

public class BookDetailFragment extends Fragment {

    public static final String ARG_BOOK = "aBook";
    private Book bookItem;

    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_BOOK)) {
            bookItem = getArguments().getParcelable(ARG_BOOK);
            final Activity activity = getActivity();
            CollapsingToolbarLayout appBarLayout =
                    (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(bookItem.getTitle());
            } else {
                FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.share_fab);
                if (fab != null) {
                    fab.show();
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(Intent.createChooser(ShareCompat.IntentBuilder
                                    .from(activity)
                                    .setType("text/plain")
                                    .setText(getString(R.string.share_text).concat(" ").concat(bookItem.getTitle()))
                                    .getIntent(), getString(R.string.action_share)));
                        }
                    });
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_detail, container, false);
        if (bookItem != null) {
            TextView title = (TextView) rootView.findViewById(R.id.book_title);
            if (title != null) {
                title.setText(bookItem.getTitle());
            }
            ((ImageView) rootView.findViewById(R.id.cover)).setImageBitmap(bookItem.getCover());
            ((TextView) rootView.findViewById(R.id.book_authors))
                    .setText(bookItem.getAuthorsForDetail());
        }

        return rootView;
    }
}
