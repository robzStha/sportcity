package com.app.sportcity.utils;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.app.sportcity.R;

/**
 * Created by bugatti on 12/12/16.
 */
public class FabInitializer {
    Activity activity;


    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2,fab3;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    public FabInitializer(Activity activity) {
        this.activity = activity;
        initialize();
    }

    private void initialize() {
        fab = (FloatingActionButton)activity.findViewById(R.id.fab);
        fab1 = (FloatingActionButton)activity.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)activity.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton)activity.findViewById(R.id.fab3);
        fab_open = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(fabClickListener);
        fab1.setOnClickListener(fabClickListener);
        fab2.setOnClickListener(fabClickListener);
        fab3.setOnClickListener(fabClickListener);
    }

    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.fab:
                    animateFAB();
                    break;
                case R.id.fab1:
                    break;
                case R.id.fab2:
                    break;
                case R.id.fab3:
                    break;
            }
        }
    };

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;

        }
    }
}
