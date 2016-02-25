package com.android.listmybooks.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.listmybooks.R;
import com.android.listmybooks.services.dropbox.FindEpubAsyncTask;
import com.android.listmybooks.services.dropbox.SessionManager;

public class LinkActivity extends AppCompatActivity {

    private static final String ACCOUNT_PREFS_NAME = "prefs";
    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    private ProgressBar spinner;
    private Button linkButton;
    private SessionManager sessionManager;

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

        // Display the proper UI state if logged in or not
        if (sessionManager.isLinked()) {
            doLogIn();
        }
    }

    private void onClickLinkToDropbox() {
        // Start the remote authentication
        sessionManager.startAuthentication(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Store it locally in our app for later use
        String authentication = sessionManager.getAuthenticationOrNull();
        if (authentication != null) {
            storeAuth(authentication);
            doLogIn();
        }
    }

    public void doLogOut() {
        // Remove credentials from the session
        sessionManager.getSession().unlink();
        // Clear our stored keys
        clearKeys();
    }

    /**
     * Convenience function to change UI state based on being logged in
     */
    private void doLogIn() {
        linkButton.setVisibility(View.GONE);
        new FindEpubAsyncTask(this.spinner, this.sessionManager.getSession(),
                getExternalFilesDir(null)).execute();
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
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