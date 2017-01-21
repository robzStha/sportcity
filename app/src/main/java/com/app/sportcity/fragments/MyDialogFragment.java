package com.app.sportcity.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.sportcity.R;

public class MyDialogFragment extends DialogFragment {
    ViewPager vpImages;
    public static MyDialogFragment newInstance() {
        return new MyDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View v = inflater.inflate(R.layout.dialog_fragment, container, false);
//            View tv = v.findViewById(R.id.text);
//            ((TextView)tv).setText("This is an instance of MyDialogFragment");
        return v;
    }
}