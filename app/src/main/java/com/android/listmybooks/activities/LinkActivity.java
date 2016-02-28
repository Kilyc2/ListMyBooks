package com.android.listmybooks.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.listmybooks.R;
import com.android.listmybooks.helpers.AlertHelper;
import com.android.listmybooks.services.dropbox.FindEpubAsyncTask;
import com.android.listmybooks.services.dropbox.SessionManager;

public class LinkActivity extends AppCompatActivity {

    private static final String ACCOUNT_PREFS_NAME = "prefs";
    private static final String ACCESS_KEY_NAME = "aKeyName";
    private static final String ACCESS_SECRET_NAME = "aSecretName";

    private Button linkButton;
    private SessionManager sessionManager;
    protected ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        sessionManager = new SessionManager(prefs.getString(ACCESS_KEY_NAME, null),
                prefs.getString(ACCESS_SECRET_NAME, null));

        setContentView(R.layout.activity_link);
        spinner = (ProgressBar) findViewById(R.id.progress_bar_search);
        linkButton = (Button) findViewById(R.id.link_button);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLinkToDropbox();
            }
        });

        if (sessionManager.isLinked()) {
            doLogIn();
        }
    }

    private void onClickLinkToDropbox() {
        sessionManager.startAuthentication(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String authentication = sessionManager.getAuthenticationOrNull();
        if (authentication != null) {
            storeAuth(authentication);
            doLogIn();
        }
    }

    public void doLogOut() {
        sessionManager.getSession().unlink();
        clearKeys();
    }

    public void onPreExecuteAsyncTask() {
        this.spinner.setVisibility(View.VISIBLE);
    }

    public void onPostExecuteAsyncTask() {
        this.spinner.setVisibility(View.INVISIBLE);
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

    private void doLogIn() {
        linkButton.setVisibility(View.INVISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new FindEpubAsyncTask(this, this.sessionManager.getSession()).execute();
    }

    protected Intent getListBooksIntent() {
        return new Intent(this, BookListActivity.class);
    }

    private void storeAuth(String oauth2AccessToken) {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, "oauth2:");
        edit.putString(ACCESS_SECRET_NAME, oauth2AccessToken);
        edit.apply();
    }

    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.apply();
    }
}