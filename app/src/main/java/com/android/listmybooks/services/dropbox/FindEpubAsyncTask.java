package com.android.listmybooks.services.dropbox;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FindEpubAsyncTask extends AsyncTask<Void, Void, List<InputStream>> {

    private ProgressBar spinner;
    private ApiManager apiManager;
    private String path;

    public FindEpubAsyncTask(ProgressBar spinner, AndroidAuthSession session,
                             File externalFilesDir) {
        this.spinner = spinner;
        this.apiManager = new ApiManager(session);
        this.path = externalFilesDir.getPath();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.spinner.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<InputStream> epubs) {
        super.onPostExecute(epubs);
        this.spinner.setVisibility(View.GONE);
    }

    @Override
    protected List<InputStream> doInBackground(Void... params) {
        List<InputStream> epubsStream = new ArrayList<>();
        List<DropboxAPI.Entry> epubs = getEpubs();
        for (DropboxAPI.Entry epub : epubs) {
            try {
                epubsStream.add(getInputStreamFromEpub(epub));
            } catch (IOException ioe) {
                Log.e("READ", "can't read ".concat(epub.fileName()));
            }
        }
        return epubsStream;
    }

    private List<DropboxAPI.Entry> getEpubs() {
        return this.apiManager.searchFiles("", ".epub");
    }

    private InputStream getInputStreamFromEpub(DropboxAPI.Entry epub) throws IOException {
        File file = getFile(epub);
        return new FileInputStream(file.getPath());
    }

    private File getFile(DropboxAPI.Entry epub) throws IOException {
        File file = new File(this.path, epub.fileName());
        createFileIfNotExists(file);
        if (prepareFileInMemory(file, epub)) {
            return file;
        }
        return new File("");
    }

    private boolean prepareFileInMemory(File file, DropboxAPI.Entry epub) {
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
