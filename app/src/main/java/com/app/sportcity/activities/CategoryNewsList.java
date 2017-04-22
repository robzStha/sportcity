package com.app.sportcity.activities;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewStructure;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sportcity.R;
import com.app.sportcity.fragments.PlaceholderFragment;
import com.app.sportcity.fragments.WebViewHolderFragment;
import com.app.sportcity.objects.Category;
import com.app.sportcity.objects.CategorySer;
import com.app.sportcity.objects.NewsList;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.statics.StaticVariables;
import com.app.sportcity.utils.DataFeeder;
import com.app.sportcity.utils.FabInitializer;
import com.app.sportcity.utils.MyMenuItemStuffListener;
import com.app.sportcity.utils.Opener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

public class CategoryNewsList extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private List<NewsList> newsLists;
    private List<CategorySer> categories_temp;
    private List<Category> categories;
    private ApiCalls apiCalls;
    private int selectedPage;
    private TextView ui_hot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_news_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new FabInitializer(this);

        apiCalls = RetrofitSingleton.getApiCalls();

        Gson gson = new Gson();
        newsLists = gson.fromJson(DataFeeder.Categories.getNewsList(), new TypeToken<List<NewsList>>() {
        }.getType());

        categories_temp = new ArrayList<>();
        categories_temp = gson.fromJson(DataFeeder.Categories.getCategories(), new TypeToken<List<CategorySer>>() {
        }.getType());
        try {
            JSONArray jaTest = new JSONArray(DataFeeder.Categories.getCategories());
            for (int i = 0; i <= jaTest.length(); i++) {
                JSONObject joTest = jaTest.getJSONObject(i);
                CategorySer categorySer = new CategorySer();
                categorySer.setCatId(joTest.getString("catId"));
                categorySer.setIsActive(joTest.getBoolean("isActive"));
                categorySer.setCatTitle(joTest.getString("catTitle"));
                categorySer.setImgUrl(joTest.getString("imgUrl"));
                categories_temp.add(categorySer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTabLayout();
//        getCategories();
    }

    private void setTabLayout() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        int pos = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pos = bundle.getInt("pos");
//                    Category category = (Category) bundle.getSerializable("category");
            setPageTitle(StaticVariables.ActiveMenuList.list.get(pos).getTitle());
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nest_scrollview);
        scrollView.setFillViewport(true);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(pos);
        mViewPager.setOffscreenPageLimit(6);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                ImageView ivCatFeatImg = (ImageView) findViewById(R.id.iv_banner);
                String imgUrl = categories_temp.get(position).getImgUrl();
                Glide.with(getApplicationContext())
                        .load(imgUrl)
                        .centerCrop()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.images)
                        .into(ivCatFeatImg);
                setPageTitle(StaticVariables.ActiveMenuList.list.get(position).getTitle());
                selectedPage = position;
            }

            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(getApplicationContext(), "Current page: "+getCurrentPager(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Toast.makeText(getApplicationContext(), "Current page: "+getCurrentPager(), Toast.LENGTH_LONG).show();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        changeFontInViewGroup(tabLayout, "fonts/nsr.ttf");
    }

    private int getCurrentPager(){
        return mViewPager.getCurrentItem();
    }

    public void setPageTitle(String pageTitle) {
        getSupportActionBar().setTitle(pageTitle);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        int id;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            id = StaticVariables.ActiveMenuList.list.get(position).getObjectId();
//            id = categories.get(selectedPage).getId();
            System.out.println("Current page = "+selectedPage+" -- id "+id);
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if(id!=1190 && id!=156) {
                return PlaceholderFragment.newInstance(id);
            }else{
                return WebViewHolderFragment.newInstance(id);
            }
        }

        @Override
        public int getCount() {
            return StaticVariables.ActiveMenuList.list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return StaticVariables.ActiveMenuList.list.get(position).getTitle();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    void changeFontInViewGroup(ViewGroup viewGroup, String fontPath) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (TextView.class.isAssignableFrom(child.getClass())) {
                CalligraphyUtils.applyFontToTextView(child.getContext(), (TextView) child, fontPath);
            } else if (ViewGroup.class.isAssignableFrom(child.getClass())) {
                changeFontInViewGroup((ViewGroup) viewGroup.getChildAt(i), fontPath);
            }
        }
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
                Opener.CartList(CategoryNewsList.this);
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
    }
    
}
