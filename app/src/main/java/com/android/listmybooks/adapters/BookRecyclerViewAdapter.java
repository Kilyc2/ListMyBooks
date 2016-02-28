package com.android.listmybooks.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.listmybooks.R;
import com.android.listmybooks.activities.BookDetailActivity;
import com.android.listmybooks.fragments.BookDetailFragment;
import com.android.listmybooks.models.Book;

import java.util.List;

public class BookRecyclerViewAdapter
        extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {

    private final List<Book> books;
    private boolean isTwoPane;
    private final FragmentManager fragmentManager;

    public BookRecyclerViewAdapter(List<Book> books, boolean isTwoPane,
                                   FragmentManager fragmentManager) {
        this.books = books;
        this.fragmentManager = fragmentManager;
        this.isTwoPane = isTwoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        boolean isLinearLayout = (boolean)parent.getTag();
        if (isLinearLayout) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_content, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_grid_content, parent, false);
        }
        return new ViewHolder(view, isLinearLayout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.book = books.get(position);
        if (holder.title != null) {
            holder.title.setText(books.get(position).getTitle());
            holder.thumbnail.setImageBitmap(books.get(position).getThumbnail());
        } else {
            holder.thumbnail.setImageBitmap(books.get(position).getCoverItem());
        }

        holder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(BookDetailFragment.ARG_BOOK, holder.book);
                    BookDetailFragment fragment = new BookDetailFragment();
                    fragment.setArguments(arguments);
                    fragmentManager.beginTransaction()
                            .replace(R.id.book_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, BookDetailActivity.class);
                    intent.putExtra(BookDetailFragment.ARG_BOOK, holder.book);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View bookView;
        public final ImageView thumbnail;
        public final TextView title;
        public Book book;

        public ViewHolder(View view, boolean isLinearLayout) {
            super(view);
            bookView = view;
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            if (isLinearLayout)
                title = (TextView) view.findViewById(R.id.title);
            else
                title = null;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }
}