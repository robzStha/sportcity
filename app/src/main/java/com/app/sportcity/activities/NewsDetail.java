package com.app.sportcity.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.objects.NewsList;
import com.app.sportcity.utils.FabInitializer;

public class NewsDetail extends AppCompatActivity {

    TextView tvTitle, tvDate, tvDesc;
    ImageView ivFav, ivShare;
    TextView btnBuyImg;

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
}
