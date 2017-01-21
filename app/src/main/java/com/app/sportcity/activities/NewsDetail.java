package com.app.sportcity.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.fragments.MyDialogFragment;
import com.app.sportcity.objects.NewsList;
import com.app.sportcity.utils.CommonMethods;
import com.app.sportcity.utils.DataFeeder;
import com.app.sportcity.utils.FabInitializer;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.URL;

public class NewsDetail extends AppCompatActivity {

    TextView tvTitle, tvDate, tvDesc;
    ImageView ivFav, ivShare;
    TextView btnBuyImg;
    RecyclerView rvImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Bundle bundle = getIntent().getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        if (bundle != null) {
            NewsList newsDetail = (NewsList) bundle.getSerializable("news_details");
            populateNewsDetail(newsDetail);
        }

        new FabInitializer(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        tvTitle = (TextView) findViewById(R.id.tv_news_title);
        tvDate = (TextView) findViewById(R.id.tv_news_date);
        tvDesc = (TextView) findViewById(R.id.tv_news_content);

        ivFav = (ImageView) findViewById(R.id.iv_fav);
        ivShare = (ImageView) findViewById(R.id.iv_share);

        rvImg = (RecyclerView) findViewById(R.id.rv_images);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvImg.setLayoutManager(linearLayoutManager);
//        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return new PageFragment().create(position);
//            }
//
//            @Override
//            public int getCount() {
//                return DataFeeder.ImageFeeder.getImages().size();
//            }
//        };
        rvImg.setAdapter(new ImageAdapter());

//        ImagePagerAdapter mCustomPagerAdapter = new ImagePagerAdapter(this, DataFeeder.ImageFeeder.getImages());
//
//        rvImg.setAdapter(mCustomPagerAdapter);
//        rvImg.setPageMargin(64);
        btnBuyImg = (TextView) findViewById(R.id.tv_buy_imgs);
        btnBuyImg.setOnClickListener(buyClickListener);
    }

    private View.OnClickListener buyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(NewsDetail.this, "Lets buy some images", Toast.LENGTH_SHORT).show();
        }
    };

    private void populateNewsDetail(NewsList newsDetail) {
        tvTitle.setText(newsDetail.getNewsTile());
        tvTitle.setVisibility(View.GONE);
        tvDate.setText(newsDetail.getPublishedDate());
        tvDesc.setText(newsDetail.getNewsDesc());
        getSupportActionBar().setTitle(newsDetail.getNewsTile());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView ivItem;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ivItem = (ImageView) itemView.findViewById(R.id.iv_item);
        }
    }

    public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.item_image, parent, false);
            return new ImageViewHolder(itemView);
        }
//        Bitmap image = null;

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        URL url = new URL(DataFeeder.ImageFeeder.getImages().get(position).getImgUrl());
//                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    } catch (IOException e) {
//                        System.out.println(e);
//                    }
//                    if (image != null) {
            Glide.with(NewsDetail.this)
                    .load(DataFeeder.ImageFeeder.getImages().get(position).getImgUrl())
                    .centerCrop()
                    .override((int) CommonMethods.pxFromDp(NewsDetail.this, 150),
                            (int) CommonMethods.pxFromDp(NewsDetail.this, 150))
                    .placeholder(R.drawable.ic_history_black_24dp)
                    .into(holder.ivItem);
//                    }
//                }
//            }).start();
            holder.ivItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
        }

        @Override
        public int getItemCount() {
            return DataFeeder.ImageFeeder.getImages().size();
        }
    }

//    private class PageFragment extends Fragment {
//
//        private static final String ARG_PAGE_NUMBER = "pageNumber";
//
//        public PageFragment create(int pageNumber) {
//            PageFragment fragment = new PageFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt(ARG_PAGE_NUMBER, pageNumber);
//            fragment.setArguments(bundle);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_page, container, false);
//            final Bundle arguments = getArguments();
//            if (arguments != null) {
//                ImageView imageView = (ImageView) rootView.findViewById(R.id.iv_pager_img);
//                Glide.with(NewsDetail.this).
//                        load(DataFeeder.ImageFeeder.getImages().get(arguments.getInt(ARG_PAGE_NUMBER)).getImgUrl()).
//                        centerCrop().
//                        into(imageView);
//            }
//
////            Button btItem = (Button) rootView.findViewById(R.id.bt_item);
////            final Bundle arguments = getArguments();
////            if (arguments != null) {
////                btItem.setText(
////                        getString(R.string.page_number_1d,
////                                arguments.getInt(ARG_PAGE_NUMBER) + 1));
////                btItem.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        new AlertDialog.Builder(getActivity())
////                                .setMessage(getString(R.string.page_number_1d,
////                                        arguments.getInt(ARG_PAGE_NUMBER) + 1))
////                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
////                                    @Override
////                                    public void onClick(DialogInterface dialog, int which) {
////                                        dialog.dismiss();
////                                    }
////                                })
////                                .show();
////                    }
////                });
////            } else {
////                btItem.setVisibility(View.GONE);
////            }
//            return rootView;
//        }


    //    }
    void showDialog() {
        // Create the fragment and show it as a dialog.
        DialogFragment newFragment = MyDialogFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
}
