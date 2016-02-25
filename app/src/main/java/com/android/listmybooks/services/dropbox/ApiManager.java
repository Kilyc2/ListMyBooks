package com.android.listmybooks.services.dropbox;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ApiManager {

    private DropboxAPI<AndroidAuthSession> dropboxApi;

    public ApiManager(AndroidAuthSession session) {
        this.dropboxApi = new DropboxAPI<>(session);
    }

    public List<DropboxAPI.Entry> searchFiles(String path, String query) {
        try {
            return dropboxApi.search(path, query, 0, false);
        } catch (DropboxException e) {
            return new ArrayList<>();
        }
    }

    public boolean downloadFile(File file, String path) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            this.dropboxApi.getFile(path, null, outputStream, null);
        } catch (FileNotFoundException | DropboxException fnfeDe) {
            return false;
        }
        return true;
    }
}
