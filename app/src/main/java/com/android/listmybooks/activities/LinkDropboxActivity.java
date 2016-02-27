package com.android.listmybooks.activities;

import android.content.Intent;

import com.android.listmybooks.helpers.AlertHelper;

public abstract class LinkDropboxActivity extends AppCompatWithSpinnerActivity {

    public void onPreExecuteAsyncTask() {
        setSpinnerVisible();
    }

    public void onPostExecuteAsyncTask() {
        setSpinnerInvisible();
        startListActivity();
    }

    public void showMessage(String message) {
        AlertHelper.showAlertShort(this.spinner, message);
    }

    public String getExternalFilesDirPath() {
        return getExternalFilesDir(null).getPath();
    }

    private void startListActivity() {
        Intent intent = getListBooksIntent();
        startActivity(intent);
        finish();
    }

    protected abstract Intent getListBooksIntent();
}
