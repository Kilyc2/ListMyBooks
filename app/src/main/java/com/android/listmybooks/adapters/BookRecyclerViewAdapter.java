package com.android.listmybooks.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.listmybooks.R;
import com.android.listmybooks.models.Book;

import java.util.List;

public class BookRecyclerViewAdapter
        extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {

    private final List<Book> books;
    private final boolean isTwoPane;

    public BookRecyclerViewAdapter(List<Book> books, boolean isTwoPane) {
        this.books = books;
        this.isTwoPane = isTwoPane;
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
                    //TODO

                } else {
                    //TODO

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