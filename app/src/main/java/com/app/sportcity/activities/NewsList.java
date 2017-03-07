package com.app.sportcity.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.sportcity.R;
import com.app.sportcity.fragments.PlaceholderFragment;

public class NewsList extends AppCompatActivity {
    int catId=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            catId = bundle.getInt("catId");
            getSupportActionBar().setTitle(bundle.getString("catTitle"));
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PlaceholderFragment hello = PlaceholderFragment.newInstance(catId);
        fragmentTransaction.add(R.id.activity_news_list, hello, "HELLO");
        fragmentTransaction.commit();
    }
}
