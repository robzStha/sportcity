package com.app.sportcity.activities;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Path;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.adapters.NewsListAdapter;
import com.app.sportcity.fragments.HomeFragment;
import com.app.sportcity.fragments.HomeNewsFragment;
import com.app.sportcity.objects.CartDetails;
import com.app.sportcity.objects.Category;
import com.app.sportcity.objects.Post;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.statics.StaticVariables;
import com.app.sportcity.utils.EndlessRecyclerOnScrollListener;
import com.app.sportcity.utils.FabInitializer;
import com.app.sportcity.utils.MyMenuItemStuffListener;
import com.app.sportcity.utils.MySharedPreference;
import com.app.sportcity.utils.Opener;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RelativeLayout rlHome;
    FrameLayout flCart;

    private Fragment mFragment;
    private TextView tvBadge;


    RecyclerView rvNewsList;
    NewsListAdapter newsListAdapter;
    EndlessRecyclerOnScrollListener scrollListener;
    private ApiCalls apiCalls;
    private List<Post> newsTemp;
    LinearLayout llProgressBar;
    private boolean hasNext;
    private TextView ui_hot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        LinearLayout llContentBase = (LinearLayout) findViewById(R.id.content_base);
        tvBadge = (TextView) findViewById(R.id.tv_badge);
        new FabInitializer(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_nav_menu);
        getAllTextView(ll);

        View view = getLayoutInflater().inflate(R.layout.fragment_category_news_list, null, false);
        llContentBase.addView(view);

        rvNewsList = (RecyclerView) findViewById(R.id.rv_cats);
        llProgressBar = (LinearLayout) findViewById(R.id.ll_progressbar);
        getLatestPost();
        rlHome = (RelativeLayout) findViewById(R.id.rl_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        Toast.makeText(BaseActivity.this, "Item id: " + id, Toast.LENGTH_SHORT).show();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        new MyMenuItemStuffListener(menu_hotlist, "ΤΟ ΚΑΛΑΘΙ ΜΟΥ") {
            @Override
            public void onClick(View v) {
                Opener.CartList(BaseActivity.this);
            }
        };
        return super.onCreateOptionsMenu(menu);
    }

    // call the updating code on the main thread,
// so we can call this asynchronously
    public void updateHotCount(final int new_hot_number) {
//        StaticVariables.Cart.cartDetails.setTotalCount(new_hot_number);
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

        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
        MySharedPreference prefs = new MySharedPreference(BaseActivity.this);
        if (prefs.getStringValues(StaticVariables.CART_ITEM) != "") {
            StaticVariables.cartDetails = new Gson().fromJson(prefs.getStringValues(StaticVariables.CART_ITEM), CartDetails.class);

            try {
                tvBadge.setText(StaticVariables.cartDetails.getTotalCount() + "");
            } catch (Exception e) {
                e.printStackTrace();
                tvBadge.setText("0");
            }
        }
//
    }

    private void getAllTextView(ViewGroup viewGroup) {

        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup)
                getAllTextView((ViewGroup) view);
            else if (view instanceof TextView) {
                final TextView edittext = (TextView) view;
                if (edittext.getTag() != null) {
                    edittext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            if (drawer.isDrawerOpen(GravityCompat.START))
                                drawer.closeDrawer(GravityCompat.START);
                            if (edittext.getTag().equals("23")) {
                                Opener.Shop(BaseActivity.this);
                            } else if (edittext.getTag().equals("24")) {
                                Opener.CartList(BaseActivity.this);
                            } else {
                                Opener.NewsList(BaseActivity.this, Integer.parseInt(v.getTag().toString()), edittext.getText().toString());
                            }
//                            Toast.makeText(BaseActivity.this, "Testing testing: " + v.getTag(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else if (view instanceof ImageView) {
                ImageView iv = (ImageView) view;
                if (iv.getTag() != null) {
                    if (iv.getTag().equals("112")) {
                        Opener.CartList(BaseActivity.this);
                    }
                }
            }
        }
    }


    private void getLatestPost() {
        apiCalls = RetrofitSingleton.getApiCalls();
        if (StaticVariables.news.size() > 0) {
            newsTemp = StaticVariables.news;
            populateNews(newsTemp);
            StaticVariables.reset();
        } else {

            final ProgressDialog pd = new ProgressDialog(BaseActivity.this);
            pd.setMessage("Loading news...");
            pd.show();
            Call<List<Post>> posts = apiCalls.getLatestPosts();
            posts.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    System.out.println("Response size:" + response.body().size());
                    newsTemp = response.body();
                    Headers headers = response.headers();
                    String temp = headers.get("Link").replace("<", "");
                    temp = temp.replace(">", "");
                    String string[] = temp.split(" ");
                    String nextLink = "";
                   // System.out.println("Next linkss : " + temp + " Split: " + string[0] + " : " + string[1]);
                    System.out.println("string 0 is : " + string[0]);
                    if (string[1].equals("rel=\"next\"")) {
                        System.out.println("Next linkss : next: " + string[1]);
                        nextLink = string[0];
                    }
                    populateNews(response.body());
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    t.printStackTrace();
                    pd.dismiss();
                }
            });
        }

    }

    private void populateNews(final List<Post> news) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvNewsList.setLayoutManager(linearLayoutManager);
        newsListAdapter = new NewsListAdapter(BaseActivity.this, news);
        scrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMoreFromAPI(current_page);
            }
        };

        try {
            rvNewsList.addOnScrollListener(scrollListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
        rvNewsList.setAdapter(newsListAdapter);
    }

    private void loadMoreFromAPI(int current_page) {
        llProgressBar.setVisibility(View.VISIBLE);
        Call<List<Post>> posts = apiCalls.getLatestPostsNext(current_page);
        posts.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                System.out.println("Response size:" + response.body().size());
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
                newsListAdapter.appendNewNews(response.body());
//                progressDialog.dismiss();
                llProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                llProgressBar.setVisibility(View.GONE);
//                progressDialog.dismiss();
            }
        });
    }


}
