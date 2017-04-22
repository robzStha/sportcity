package com.app.sportcity.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.sportcity.R;
import com.app.sportcity.adapters.ImagesAdapter;
import com.app.sportcity.fragments.SlideshowDialogFragment;
import com.app.sportcity.objects.ACF;
import com.app.sportcity.objects.Media;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.statics.StaticVariables;
import com.app.sportcity.utils.EndlessRecyclerOnScrollListener;
import com.app.sportcity.utils.MyMenuItemStuffListener;
import com.app.sportcity.utils.MySharedPreference;
import com.app.sportcity.utils.Opener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Images extends AppCompatActivity {

    RecyclerView recyclerView;
    ImagesAdapter imgAdapter;
    private ApiCalls apicall;
    public static ArrayList<Media> mediaListShop = new ArrayList<>();
    private TextView ui_hot;
    EndlessRecyclerOnScrollListener scrollListener;
    private boolean hasNext;
    private LinearLayout llProgressBar;
    private final static int IMAGES_PER_PAGE = 100;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        loadImages();

    }

    private void loadImages() {
        apicall = RetrofitSingleton.getApiCalls();
        final ProgressDialog progressDialog = new ProgressDialog(Images.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        final Call<List<Media>> mediaList = apicall.getMediaList(IMAGES_PER_PAGE);
        mediaList.enqueue(new Callback<List<Media>>() {
            @Override
            public void onResponse(Call<List<Media>> call, Response<List<Media>> response) {
                System.out.println("Responsesssss: " + response.body().size());

                mediaListShop = new ArrayList<Media>();
                for (Media media : response.body()) {
                    System.out.println("ACF value: " + media.getAcf().toString());
                    List<ACF> acfList = media.getAcf();
                    if (acfList.size() > 0) {
                        ACF acf = acfList.get(0);
                        if (acf.getShowInStore().equalsIgnoreCase("yes")) {
                            mediaListShop.add(media);
                        }
                    }
                    System.out.println("Responsesssss shop list: " + mediaListShop.size());
                }
                if (mediaListShop.size() > 0) {
                    imgAdapter = new ImagesAdapter(Images.this, mediaListShop);
                    GridLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    scrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                        @Override
                        public void onLoadMore(int current_page) {
                            if (hasNext)
                                loadMoreImagesFromAPI(current_page);
                            else
                                llProgressBar.setVisibility(View.GONE);
                        }
                    };

                    try {
                        recyclerView.addOnScrollListener(scrollListener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    recyclerView.setAdapter(imgAdapter);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Media>> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void loadMoreImagesFromAPI(int current_page) {
        System.out.println("Current page: "+current_page);
        llProgressBar.setVisibility(View.VISIBLE);
        final Call<List<Media>> mediaList = apicall.getMediaListNext(IMAGES_PER_PAGE, current_page);
        mediaList.enqueue(new Callback<List<Media>>() {
            @Override
            public void onResponse(Call<List<Media>> call, Response<List<Media>> response) {
                System.out.println("Responsesssss: " + response.body().size());
                Headers headers = response.headers();
                String temp = headers.get("Link").replace("<", "");
                temp = temp.replace(">", "");
                String string[] = temp.split(" ");
                System.out.println("Next linkss : " + temp + " Split: " + string[0] + " : " + string[1]);
                if (string.length == 2) {
                    if (string[1].equals("rel=\"next\"")) {
                        hasNext = true;
                    } else hasNext = false;
                } else if (string.length == 4) {
                    if (string[3].equals("rel=\"next\"")) {
                        hasNext = true;
                    } else hasNext = false;
                }
                imgAdapter.appendNewImages(response.body());
                llProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Media>> call, Throwable t) {
                t.printStackTrace();
                llProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {
        prefs = new MySharedPreference(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Images.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        llProgressBar = (LinearLayout) findViewById(R.id.ll_progressbar);
        recyclerView.addOnItemTouchListener(new ImagesAdapter.RecyclerTouchListener(Images.this, recyclerView, new ImagesAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Toast.makeText(Images.this, "Position: " + position, Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance(Images.this);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        System.out.println("Stop: " + gson.toJson(StaticVariables.Cart.cartDetails));
        prefs.setKeyValues(StaticVariables.CART_ITEM, gson.toJson(StaticVariables.cartDetails) + "");
    }

    Gson gson = new Gson();
    MySharedPreference prefs;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mediaListShop = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.clear();
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_hotlist);
        MenuItemCompat.setActionView(item, R.layout.action_bar_icon_notification);
        View menu_hotlist = MenuItemCompat.getActionView(item);
        ui_hot = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
        updateHotCount(StaticVariables.cartDetails.getTotalCount());
        new MyMenuItemStuffListener(menu_hotlist, getString(R.string.cart)) {
            @Override
            public void onClick(View v) {
                Opener.CartList(Images.this);
            }
        };
        return super.onCreateOptionsMenu(menu);
    }

    // call the updating code on the main thread,
// so we can call this asynchronously
    public void updateHotCount(final int new_hot_number) {
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

    @Override
    protected void onResume() {
        super.onResume();
        updateHotCount(StaticVariables.cartDetails.getTotalCount());
    }
}
