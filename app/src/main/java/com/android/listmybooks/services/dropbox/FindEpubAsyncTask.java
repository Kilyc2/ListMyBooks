package com.android.listmybooks.services.dropbox;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.AsyncTask;

import com.android.listmybooks.activities.LinkActivity;
import com.android.listmybooks.data.BooksTable;
import com.android.listmybooks.helpers.DateHelper;
import com.android.listmybooks.helpers.FileHelper;
import com.android.listmybooks.models.Book;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class FindEpubAsyncTask extends AsyncTask<Void, Void, Void> {

    private ApiManager apiManager;
    WeakReference<Activity> weakActivity;

    public FindEpubAsyncTask(LinkActivity activity, AndroidAuthSession session) {
        this.apiManager = new ApiManager(session);
        this.weakActivity = new WeakReference<Activity>(activity);
    }

    @Override
    protected void onPreExecute() {
        Activity activity = this.weakActivity.get();
        if (activity != null) {
            super.onPreExecute();
            ((LinkActivity)activity).onPreExecuteAsyncTask();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Activity activity = this.weakActivity.get();
        if (activity != null) {
            super.onPostExecute(aVoid);
            ((LinkActivity)activity).onPostExecuteAsyncTask();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        ((LinkActivity)this.weakActivity.get()).showMessage("Searching .epub files from Dropbox");
        List<Entry> epubs = getEpubsFromApi();
        ((LinkActivity)this.weakActivity.get()).showMessage("Preparing library");
        for (Entry epub : epubs) {
            try {
                prepareEpub(epub);
            } catch (IOException ioe) {
                ((LinkActivity)this.weakActivity.get()).showMessage("Can't open ".concat(epub.fileName()));
            }
        }
        return null;
    }

    private List<DropboxAPI.Entry> getEpubsFromApi() {
        return this.apiManager.searchFiles("", ".epub");
    }

    private boolean prepareEpub(Entry epub) throws IOException {
        File file = FileHelper.createFile(((LinkActivity)this.weakActivity.get())
                .getExternalFilesDirPath(), epub.fileName());
        return prepareEpubInMemory(epub, file);
    }

    private boolean prepareEpubInMemory(Entry epub, File file) {
        return isSizeEquals(epub.bytes, file.length()) || saveBookInMemory(epub, file);
    }

    private boolean saveBookInMemory(Entry epub, File file) {
        ((LinkActivity)this.weakActivity.get()).showMessage(epub.fileName()
                .concat(" is being added to the library"));
        boolean isDonwloaded = this.apiManager.downloadFile(file, epub.path);
        if (isDonwloaded) {
            Book book = new Book(file.getPath(), DateHelper.getDateFormated(epub.modified));
            saveBookInDB(book);
        }
        return isDonwloaded;
    }

    private void saveBookInDB(Book book) {
        ContentResolver contentResolver = this.weakActivity.get().getContentResolver();
        contentResolver.insert(BooksTable.getContentUri(), BooksTable.getValuesBook(book));
    }

    private boolean isSizeEquals(long fileSize, long entrySize) {
        return fileSize == entrySize;
    }
}
