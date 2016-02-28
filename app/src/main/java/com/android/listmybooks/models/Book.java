package com.android.listmybooks.models;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.listmybooks.services.epubreader.EpubManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Book implements Parcelable {

    private final static String SEPARATOR = ";";

    private long id;
    private String title;
    private List<String> authors;
    private String coverPath;
    private String date;

    public Book(String path, String date) {
        id = 0;
        EpubManager epubManager = new EpubManager(path);
        setTitle(epubManager.getTitle());
        setAuthors(epubManager.getAuthors());
        setCoverPath(epubManager.getCoverImagePath());
        setDate(date);
    }

    public Book(long id, String title, String authors, String coverPath, String date) {
        this.id = id;
        this.title = title;
        setAuthors(authors);
        this.coverPath = coverPath;
        this.date = date;
    }

    protected Book(Parcel in) {
        readToParcel(in);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    private void setTitle(String title) {
        if (title.isEmpty())
            title = "Without Title";
        this.title = title;
    }

    private void setAuthors(List<String> authors) {
        if (authors.isEmpty()) {
            authors.add("Anonymous");
        }
        this.authors = authors;
    }

    private void setAuthors(String authors) {
        this.authors = new ArrayList<>();
        if (authors.isEmpty()) {
            this.authors.add("Anonymous");
        } else {
            Collections.addAll(this.authors, authors.split(SEPARATOR));
        }
    }

    private void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    private void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getAuthorsForDetail() {
        String authorsString = "";
        for (String author : authors) {
            authorsString = authorsString.concat(author).concat("\n");
        }
        return authorsString;
    }

    public String getAuthorsForDb() {
        String authorsString = "";
        for (String author : authors) {
            authorsString = authorsString.concat(author).concat(SEPARATOR);
        }
        return authorsString;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public Bitmap getCover() {
        return Bitmap.createScaledBitmap(getCoverAsBitmap(), 400, 600, true);
    }

    public Bitmap getThumbnail() {
        return Bitmap.createScaledBitmap(getCoverAsBitmap(), 150, 200, true);
    }

    public Bitmap getCoverItem() {
        return Bitmap.createScaledBitmap(getCoverAsBitmap(), 200, 300, true);
    }

    private Bitmap getCoverAsBitmap() {
        if (this.coverPath.isEmpty()) {
            return null;
        }
        return BitmapFactory.decodeFile(this.coverPath, new BitmapFactory.Options());
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeStringList(authors);
        dest.writeString(coverPath);
        dest.writeString(date);
    }

    private void readToParcel(Parcel in) {
        id = in.readLong();
        title = in.readString();
        authors = in.createStringArrayList();
        coverPath = in.readString();
        date = in.readString();
    }
}
