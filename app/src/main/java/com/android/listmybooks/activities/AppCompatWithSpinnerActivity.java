package com.android.listmybooks.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

public class AppCompatWithSpinnerActivity extends AppCompatActivity {

    protected ProgressBar spinner;

    protected void setSpinnerVisible() {
        this.spinner.setVisibility(View.VISIBLE);
    }

    protected void setSpinnerInvisible() {
        this.spinner.setVisibility(View.INVISIBLE);
    }

}
