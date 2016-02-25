package com.android.listmybooks.services.dropbox;

import android.content.Context;

import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

public class SessionManager {

    private AndroidAuthSession session;

    public SessionManager(String key, String secret) {
        this.session = buildSession(key, secret);
    }

    private AndroidAuthSession buildSession(String key, String secret) {
        AndroidAuthSession session = new AndroidAuthSession(getAppKeyPair());
        loadAuth(session, key, secret);
        return session;
    }

    private AppKeyPair getAppKeyPair() {
        final String APP_KEY = "qwbz5mifusyu9ek";
        final String APP_SECRET = "wg3rlj12rfdyngm";

        return new AppKeyPair(APP_KEY, APP_SECRET);
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void loadAuth(AndroidAuthSession session, String key, String secret) {
        if (key == null || secret == null || key.length() == 0 || secret.length() == 0) return;
        session.setOAuth2AccessToken(secret);
    }

    public AndroidAuthSession getSession() {
        return this.session;
    }

    public boolean isLinked() {
        return this.session.isLinked();
    }

    public void startAuthentication(Context context) {
        this.session.startOAuth2Authentication(context);
    }

    public String getAuthenticationOrNull() {
        // The next part must be inserted in the onResume() method of the
        // activity from which session.startAuthentication() was called, so
        // that Dropbox authentication completes properly.
        if (this.session.authenticationSuccessful()) {
            // Mandatory call to complete the auth
            this.session.finishAuthentication();
            return this.session.getOAuth2AccessToken();
        }
        return null;
    }

}
