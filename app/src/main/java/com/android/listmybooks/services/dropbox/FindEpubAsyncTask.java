package com.android.listmybooks.services.dropbox;

import android.content.ContentResolver;
import android.os.AsyncTask;

import com.android.listmybooks.activities.LinkDropboxActivity;
import com.android.listmybooks.data.BooksTable;
import com.android.listmybooks.helpers.FileHelper;
import com.android.listmybooks.models.Book;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FindEpubAsyncTask extends AsyncTask<Void, Void, Void> {

    private LinkDropboxActivity activity;
    private ApiManager apiManager;

    public FindEpubAsyncTask(LinkDropboxActivity activity, AndroidAuthSession session) {
        this.activity = activity;
        this.apiManager = new ApiManager(session);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.activity.onPreExecuteAsyncTask();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        this.activity.onPostExecuteAsyncTask();
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.activity.showMessage("Searching .epub files from Dropbox");
        List<Entry> epubs = getEpubsFromApi();
        this.activity.showMessage("Preparing library");
        for (Entry epub : epubs) {
            try {
                prepareEpub(epub);
            } catch (IOException ioe) {
                this.activity.showMessage("Can't open ".concat(epub.fileName()));
            }
        }
        return null;
    }

    private List<DropboxAPI.Entry> getEpubsFromApi() {
        return this.apiManager.searchFiles("", ".epub");
    }

    private boolean prepareEpub(Entry epub) throws IOException {
        File file = FileHelper.createFile(this.activity.getExternalFilesDirPath(), epub.fileName());
        return prepareEpubInMemory(epub, file);
    }

    private boolean prepareEpubInMemory(Entry epub, File file) {
        return isSizeEquals(epub.bytes, file.length()) || saveBookInMemory(epub, file);
    }

    private boolean saveBookInMemory(Entry epub, File file) {
        this.activity.showMessage(epub.fileName().concat(" is being added to the library"));
        boolean isDonwloaded = this.apiManager.downloadFile(file, epub.path);
        if (isDonwloaded) {
            Book book = new Book(file.getPath(), epub.modified);
            saveBookInDB(book);
        }
        return isDonwloaded;
    }

    private void saveBookInDB(Book book) {
        ContentResolver contentResolver = this.activity.getContentResolver();
        contentResolver.insert(BooksTable.getContentUri(), BooksTable.getValuesBook(book));
    }

    private boolean isSizeEquals(long fileSize, long entrySize) {
        return fileSize == entrySize;
    }
}
