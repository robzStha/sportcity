package com.app.sportcity.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.activities.Images;
import com.app.sportcity.objects.ItemDetail;
import com.app.sportcity.statics.StaticVariables;
import com.app.sportcity.utils.MyCart;
import com.app.sportcity.utils.MySharedPreference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblPrice;
    ImageView ivAddToCart;
    private int selectedPosition = 0;
    MySharedPreference prefs;
    static Images images;

    public static SlideshowDialogFragment newInstance(Images image) {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        images = image;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prefs = new MySharedPreference(getContext());
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblPrice = (TextView) v.findViewById(R.id.price);
        ivAddToCart = (ImageView) v.findViewById(R.id.iv_add_to_cart);
//        lblDate = (TextView) v.findViewById(R.id.date);


        selectedPosition = getArguments().getInt("position");
//
//        Log.e(TAG, "position: " + selectedPosition);
//        Log.e(TAG, "images size: " + Images.mediaListShop.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        images.updateHotCount(StaticVariables.cartDetails.getTotalCount());
        images = null;
//        Toast.makeText(getContext(), "Cancled", Toast.LENGTH_SHORT).show();
    }

    private void displayMetaInfo(final int position) {
        lblCount.setText((position + 1) + " of " + Images.mediaListShop.size());
//        Image image = images.get(position);
        lblTitle.setText(Images.mediaListShop.get(position).getTitle().getRendered());
        lblPrice.setText("€" + Images.mediaListShop.get(position).getAcf().get(0).getPrice());
        ivAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetail itemDetail = new ItemDetail();
                itemDetail.setItemId(Images.mediaListShop.get(position).getId());
                itemDetail.setItemName(Images.mediaListShop.get(position).getTitle().getRendered());
                itemDetail.setItemPrice(Float.parseFloat(Images.mediaListShop.get(position).getAcf().get(0).getPrice()));
                itemDetail.setItemImgUrl(Images.mediaListShop.get(position).getMediaDetails().getSizes().getThumbnail().getSourceUrl());
                itemDetail.setItemOrgImgUrl(Images.mediaListShop.get(position).getMediaDetails().getSizes().getFull().getSourceUrl());
                itemDetail.setItemQty(1);
                itemDetail.setItemTotal(itemDetail.getItemPrice());
                if(MyCart.getInstance().addItemToCart(itemDetail)) {
                    Toast.makeText(getContext(), "ΠΡΟΣΤΕΘΗΚΕ ΣΤΟ ΚΑΛΑΘΙ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "ΥΠΑΡΧΕΙ ΗΔΗ ΣΤΟ ΚΑΛΑΘΙ", Toast.LENGTH_SHORT).show();
                }
                System.out.println("Cart info: item - "+StaticVariables.cartDetails.getTotalCount()+" total - "+StaticVariables.cartDetails.getTotalAmount());
            }
        });
//        lblDate.setText(image.getTimestamp());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

//            Image image = images.get(position);

            Glide.with(getActivity()).load(Images.mediaListShop.get(position).getMediaDetails().getSizes().getFull().getSourceUrl())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return Images.mediaListShop.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Gson gson = new Gson();
        prefs.setKeyValues(StaticVariables.CART_ITEM, gson.toJson(StaticVariables.cartDetails));
    }
}