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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.fragments.MyDialogFragment;
import com.app.sportcity.objects.Img;
import com.app.sportcity.objects.NewsList;
import com.app.sportcity.objects.Post;
import com.app.sportcity.utils.CommonMethods;
import com.app.sportcity.utils.DataFeeder;
import com.app.sportcity.utils.FabInitializer;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.URL;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewsDetail extends AppCompatActivity {

    TextView tvTitle, tvDate;
    WebView tvDesc;
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

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        if (bundle != null) {
            Post newsDetail = (Post) bundle.getSerializable("news_details");
            populateNewsDetail(newsDetail);
        }

        new FabInitializer(this);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

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
        tvDesc = (WebView) findViewById(R.id.tv_news_content);

        ivFav = (ImageView) findViewById(R.id.iv_fav);
        ivShare = (ImageView) findViewById(R.id.iv_share);

        rvImg = (RecyclerView) findViewById(R.id.rv_images);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvImg.setLayoutManager(linearLayoutManager);
        rvImg.setAdapter(new ImageAdapter());
        btnBuyImg = (TextView) findViewById(R.id.tv_buy_imgs);
        btnBuyImg.setOnClickListener(buyClickListener);
    }

    private View.OnClickListener buyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(NewsDetail.this, "Lets buy some images", Toast.LENGTH_SHORT).show();
        }
    };

    private void populateNewsDetail(Post newsDetail) {
        tvTitle.setText(Html.fromHtml(newsDetail.getTitle().getRendered()));
        tvTitle.setVisibility(View.GONE);
        tvDate.setText(newsDetail.getDate());
        tvDesc.getSettings().setJavaScriptEnabled(true);
        String temp = "<Html><Head></Head><Body>"+newsDetail.getContent().getRendered()+"</body></html>";
        tvDesc.loadData(temp, "text/html; charset=utf-8", "UTF-8");
//        tvDesc.setText(Html.fromHtml(newsDetail.getContent().getRendered()));
        getSupportActionBar().setTitle(Html.fromHtml(newsDetail.getTitle().getRendered()));
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
        public void onBindViewHolder(ImageViewHolder holder, final int position) {

            Glide.with(NewsDetail.this)
                    .load(DataFeeder.ImageFeeder.getImages().get(position).getImgUrl())
                    .centerCrop()
                    .override((int) CommonMethods.pxFromDp(NewsDetail.this, 150),
                            (int) CommonMethods.pxFromDp(NewsDetail.this, 150))
                    .placeholder(R.drawable.ic_history_black_24dp)
                    .into(holder.ivItem);
            holder.ivItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(DataFeeder.ImageFeeder.getImages().get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return DataFeeder.ImageFeeder.getImages().size();
        }
    }

    void showDialog(Img img) {
        // Create the fragment and show it as a dialog.
        DialogFragment newFragment = MyDialogFragment.newInstance(img);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
