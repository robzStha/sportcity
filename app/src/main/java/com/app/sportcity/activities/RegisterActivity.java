package com.app.sportcity.activities;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.app.sportcity.R;
import com.app.sportcity.utils.CommonMethods;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton ibtnCapturePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_white_48dp);

        ibtnCapturePhoto = (ImageButton) findViewById(R.id.ibtnCapturePhoto);
        ibtnCapturePhoto.setOnClickListener(this);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtnCapturePhoto:
                CommonMethods.getPickImageChooserIntent(RegisterActivity.this);
                break;
        }
    }
}
