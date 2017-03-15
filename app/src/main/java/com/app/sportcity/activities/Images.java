package com.app.sportcity.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.adapters.ImagesAdapter;
import com.app.sportcity.fragments.SlideshowDialogFragment;

public class Images extends AppCompatActivity {

    RecyclerView recyclerView;
    ImagesAdapter imgAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Images.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imgAdapter = new ImagesAdapter(Images.this);
        recyclerView.setAdapter(imgAdapter);

        recyclerView.addOnItemTouchListener(new ImagesAdapter.RecyclerTouchListener(Images.this, recyclerView, new ImagesAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(Images.this, "Position: "+position, Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
//                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");

            }

            @Override
            public void onLongClick(View view, int position) {
//                Toast.makeText(Images.this, "Long clicked Position: "+position, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
