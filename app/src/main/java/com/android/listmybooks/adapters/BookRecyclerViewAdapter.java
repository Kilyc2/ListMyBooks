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
    private final boolean isTwoPane;
    private final FragmentManager fragmentManager;

    public BookRecyclerViewAdapter(List<Book> books, boolean isTwoPane,
                                   FragmentManager fragmentManager) {
        this.books = books;
        this.isTwoPane = isTwoPane;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.book = books.get(position);
        holder.thumbnail.setImageBitmap(books.get(position).getThumbnail());
        holder.title.setText(books.get(position).getTitle());

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

        public ViewHolder(View view) {
            super(view);
            bookView = view;
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }
}