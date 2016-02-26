package com.android.listmybooks.models;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.listmybooks.helpers.DateHelper;
import com.android.listmybooks.services.epubreader.EpubManager;

import java.util.Date;
import java.util.List;

public class Book implements Parcelable {

    private String title;
    private List<String> authors;
    private String coverPath;
    private String date;

    public Book(String path, String date) {
        EpubManager epubManager = new EpubManager(path);
        setTitle(epubManager.getTitle());
        setAuthors(epubManager.getAuthors());
        setCoverPath(epubManager.getCoverImagePath());
        setDate(date);
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

    private void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    private void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public Bitmap getCover() {
        if (this.coverPath.isEmpty()) {
            return null;
        }
        return BitmapFactory.decodeFile(this.coverPath, new BitmapFactory.Options());
    }

    public Bitmap getThumbnail() {
        return Bitmap.createScaledBitmap(getCover(), 150, 200, true);
    }

    public Date getDate() {
        return DateHelper.parseDate(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringList(authors);
        dest.writeString(coverPath);
        dest.writeString(date);
    }

    private void readToParcel(Parcel in) {
        title = in.readString();
        authors = in.createStringArrayList();
        coverPath = in.readString();
        date = in.readString();
    }
}
