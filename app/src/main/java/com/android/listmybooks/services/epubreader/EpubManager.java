package com.android.listmybooks.services.epubreader;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.listmybooks.helpers.FileHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class EpubManager {

    private Book book;
    private String epubPath;

    public EpubManager(String epubPath) {
        this.epubPath = epubPath;
        try {
            InputStream epubInputStream = new FileInputStream(this.epubPath);
            this.book = (new EpubReader()).readEpub(epubInputStream);
        } catch (IOException e) {
            this.book = new Book();
        }
    }

    public List<String> getAuthors() {
        List<Author> authors = this.book.getMetadata().getAuthors();
        List<String> authorsCompletedName = new ArrayList<>();
        for (Author author : authors) {
            authorsCompletedName.add(author.toString());
        }
        return authorsCompletedName;
    }

    public String getTitle() {
        return this.book.getTitle();
    }

    public String getCoverImagePath() {
        try {
            return saveImage(book.getCoverImage().getInputStream());
        } catch (IOException e) {
            return "";
        }
    }

    private String saveImage(InputStream is) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return FileHelper.saveBitmap(bitmap, getFormatedPath(this.epubPath), this.book.getTitle());
    }

    private String getFormatedPath(String epubPath) {
        return epubPath.substring(0, epubPath.lastIndexOf("/"));
    }
}
