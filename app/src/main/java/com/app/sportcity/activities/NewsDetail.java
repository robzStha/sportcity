package com.app.sportcity.activities;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.fragments.MyDialogFragment;
import com.app.sportcity.objects.Img;
import com.app.sportcity.objects.Post;
import com.app.sportcity.utils.CommonMethods;
import com.app.sportcity.utils.DataFeeder;
import com.app.sportcity.utils.FabInitializer;
import com.app.sportcity.utils.Opener;
import com.bumptech.glide.Glide;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewsDetail extends AppCompatActivity {

    TextView tvTitle, tvDate;
    WebView wvDesc;
    ImageView ivFav, ivShare;
    TextView btnBuyImg;
    RecyclerView rvImg;
    private TextView ui_hot;
    private int hot_number=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_new);

        Bundle bundle = getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        if (bundle != null) {
            final Post newsDetail = (Post) bundle.getSerializable("news_details");
            populateNewsDetail(newsDetail);

            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Opener.OpenShareSheet(NewsDetail.this, "I found this news interesting. Please read this link.\n"+newsDetail.getGuid().getRendered());
                }
            });
        }

        new FabInitializer(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        tvTitle = (TextView) findViewById(R.id.tv_news_title);
        tvDate = (TextView) findViewById(R.id.tv_news_date);
//        wvDesc = (WebView) findViewById(R.id.tv_news_content);

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
            Opener.Shop(NewsDetail.this);
        }
    };

    private void populateNewsDetail(Post newsDetail) {
        tvTitle.setText(Html.fromHtml(newsDetail.getTitle().getRendered()));
        String elapsedTime = CommonMethods.timeElapsed(newsDetail.getDate().replace("T", " "));
        tvDate.setText(elapsedTime);






//        wvDesc.setWebChromeClient(new WebChromeClient(){});
//        wvDesc.getSettings().setJavaScriptEnabled(true);
//        String temp = "<html><body>Video From YouTube<br><iframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/Ry8fFmON_GY?autoplay=0&loop=0&rel=0\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        String temp = "<Html><Head><style>img{display: inline;height: auto;max-width: 100%;} iframe{width:100% !important;}</style></Head><Body>"+newsDetail.getContent().getRendered()+"</body></html>";
//        wvDesc.loadData(temp, "text/html; charset=utf-8", "utf-8");

        temp = temp.replace("'", "\"");
        temp = temp.replace("//www.youtube.com/", "https://www.youtube.com/");

        WebView displayYoutubeVideo = (WebView) findViewById(R.id.tv_news_content);
        displayYoutubeVideo.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
//        displayYoutubeVideo.getSettings().setLoadWithOverviewMode(true);
//        displayYoutubeVideo.getSettings().setUseWideViewPort(true);
        WebSettings webSettings = displayYoutubeVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayYoutubeVideo.loadData(temp, "text/html; charset=utf-8", "utf-8");

        System.out.println("Temp: "+temp);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_news_detail, menu);
        View menu_hotlist = menu.findItem(R.id.menu_hotlist).getActionView();
        ui_hot = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
        updateHotCount(hot_number);
        new MyMenuItemStuffListener(menu_hotlist, "Show hot message") {
            @Override
            public void onClick(View v) {
//                onHotlistSelected();
            }
        };
        return super.onCreateOptionsMenu(menu);
    }

    // call the updating code on the main thread,
// so we can call this asynchronously
    public void updateHotCount(final int new_hot_number) {
        hot_number = new_hot_number;
        if (ui_hot == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_hot_number == 0)
                    ui_hot.setVisibility(View.INVISIBLE);
                else {
                    ui_hot.setVisibility(View.VISIBLE);
                    ui_hot.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }

    static abstract class MyMenuItemStuffListener implements View.OnClickListener, View.OnLongClickListener {
        private String hint;
        private View view;

        MyMenuItemStuffListener(View view, String hint) {
            this.view = view;
            this.hint = hint;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override abstract public void onClick(View v);

        @Override public boolean onLongClick(View v) {
            final int[] screenPos = new int[2];
            final Rect displayFrame = new Rect();
            view.getLocationOnScreen(screenPos);
            view.getWindowVisibleDisplayFrame(displayFrame);
            final Context context = view.getContext();
            final int width = view.getWidth();
            final int height = view.getHeight();
            final int midy = screenPos[1] + height / 2;
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            Toast cheatSheet = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
            if (midy < displayFrame.height()) {
                cheatSheet.setGravity(Gravity.TOP | Gravity.RIGHT,
                        screenWidth - screenPos[0] - width / 2, height);
            } else {
                cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
            }
            cheatSheet.show();
            return true;
        }
    }
}
