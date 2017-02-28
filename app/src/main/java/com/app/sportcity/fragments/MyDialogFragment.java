package com.app.sportcity.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.applications.MyApplication;
import com.app.sportcity.objects.Img;
import com.app.sportcity.utils.Cart;
import com.app.sportcity.utils.CommonMethods;

import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;

public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
    ViewPager vpImages;

    Activity activity;

    ImageButton ibtnCross, ibtnFav;
    Button btnAddToCart;
    TextView tvPrice;
    ImageView ivItem;
    ProgressBar progressBar;

    Img img;
    Cart cart;

    public static MyDialogFragment newInstance(Img img) {

        MyDialogFragment myDialogFragment = new MyDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("img", img);
        myDialogFragment.setArguments(bundle);
        return myDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v = inflater.inflate(R.layout.dialog_fragment, container, false);
        img = (Img) getArguments().getSerializable("img");
        init(v);
        return v;
    }

    private void init(View v) {
        cart = Cart.getInstance();
        cart.setContext(getActivity());

        ibtnCross = (ImageButton) v.findViewById(R.id.ibtn_cross);
        ibtnFav = (ImageButton) v.findViewById(R.id.ibtn_fav);
        btnAddToCart = (Button) v.findViewById(R.id.btn_buy);
        tvPrice = (TextView) v.findViewById(R.id.tv_price);
        ivItem = (ImageView) v.findViewById(R.id.iv_item);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
//        if(CommonMethods.hasConnection(getActivity())) {
        if(MyApplication.hasNetwork()) {
            new BitmapDownloader().execute(img.getImgUrl());
        }

        tvPrice.setText("Price: " + img.getImgPrice());
        if (img.getIsFav().equals("true"))
            ibtnFav.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_favorite_black_48dp));
        else
            ibtnFav.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_fav_grey));
        ibtnCross.setOnClickListener(this);

        btnAddToCart.setOnClickListener(this);


//        }else{
////            this.dismiss();
//            Toast.makeText(getActivity(), "No internet connection. Please connect to internet and please try again", Toast.LENGTH_LONG).show();
//        }

    }

    private class BitmapDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap image = null;
            try {
                URL url = new URL(img.getImgUrl());
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                System.out.println(e);
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            scaleImage(bitmap);
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ibtn_cross:
                MyDialogFragment.this.dismiss();
                break;
            case R.id.btn_buy:
                Toast.makeText(getActivity(), "Work on progress. You can buy this soon.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_price:
                break;
        }

    }

    private void scaleImage(Bitmap image) throws NoSuchElementException {
        Bitmap bitmap = image;
        Point point = CommonMethods.getDisplaySize(activity);

        int newWidth = point.x - 40;
        int newHeight = CommonMethods.getRatioHeight(point.x, bitmap.getWidth(), bitmap.getHeight());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        scaledBitmap = addWhiteBorder(scaledBitmap, (int) CommonMethods.pxFromDp(getActivity(), 5));
        ivItem.setImageBitmap(scaledBitmap);
    }

    private Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}