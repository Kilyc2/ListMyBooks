package com.android.listmybooks.services.dropbox;

import android.os.AsyncTask;
import android.util.Log;

import com.android.listmybooks.activities.LinkActivity;
import com.android.listmybooks.helpers.FileHelper;
import com.android.listmybooks.models.Book;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FindEpubAsyncTask extends AsyncTask<Void, Void, ArrayList<Book>> {

    private LinkActivity activity;
    private ApiManager apiManager;

    public FindEpubAsyncTask(LinkActivity activity, AndroidAuthSession session) {
        this.activity = activity;
        this.apiManager = new ApiManager(session);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.activity.setSpinnerVisible();
    }

    @Override
    protected void onPostExecute(ArrayList<Book> books) {
        super.onPostExecute(books);
        this.activity.setSpinnerInvisible();
        this.activity.startListActivity(books);
    }

    @Override
    protected ArrayList<Book> doInBackground(Void... params) {
        ArrayList<Book> books = new ArrayList<>();
        List<Entry> epubs = getEpubs();
        for (Entry epub : epubs) {
            try {
                String path = getEpubPath(epub);
                String date = epub.modified;
                books.add(new Book(path, date));
            } catch (IOException ioe) {
                Log.e("READ", "can't read ".concat(epub.fileName()));
            }
        }
        return books;
    }

    private List<DropboxAPI.Entry> getEpubs() {
        return this.apiManager.searchFiles("", ".epub");
    }

    private String getEpubPath(Entry epub) throws IOException {
        File file = getFile(epub);
        return file.getPath();
    }

    private File getFile(Entry epub) throws IOException {
        File file = FileHelper.createFile(this.activity.getExternalFilesDirPath(), epub.fileName());
        if (prepareFileInMemory(file, epub)) {
            return file;
        }
        return new File("");
    }

    private boolean prepareFileInMemory(File file, Entry epub) {
        return isSizeEquals(file.length(), epub.bytes) ||
                this.apiManager.downloadFile(file, epub.path);
    }

    private boolean isSizeEquals(long fileSize, long entrySize) {
        return fileSize == entrySize;
    }

    private void createFileIfNotExists(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
    }

}
