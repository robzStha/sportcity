package com.app.sportcity.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.fragments.HomeFragment;
import com.app.sportcity.fragments.HomeNewsFragment;
import com.app.sportcity.objects.Category;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.utils.Opener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static Button btnCartCount;
    static int cartCount = 10;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        LinearLayout llContentBase = (LinearLayout) findViewById(R.id.content_base);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_nav_menu);
        getAllTextView(ll);

        mFragment = new HomeNewsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(llContentBase.getId(), mFragment).commit();

//        ApiCalls apiCalls = RetrofitSingleton.getApiCalls();
//        Call<List<Category>> categoryCall = apiCalls.getCategories();
//        categoryCall.enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//                System.out.println("Response size:" + response.body().size());
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        menu.clear();
//        getMenuInflater().inflate(R.menu.base, menu);
//        MenuItem item = menu.findItem(R.id.action_cart);
//        MenuItemCompat.setActionView(item, R.layout.cart_badge_count);
//        View view = MenuItemCompat.getActionView(item);
//        btnCartCount = (Button) view.findViewById(R.id.btnCartCount);
//        btnCartCount.setText(String.valueOf(cartCount));
        return true;
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

        Toast.makeText(BaseActivity.this, "Item id: " + id, Toast.LENGTH_SHORT).show();
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

    private void setNotifCount(int count) {
        cartCount = count;
        invalidateOptionsMenu();
    }

    private void getAllTextView(ViewGroup viewGroup) {

        SparseArray<EditText> array = new SparseArray<>();
//    private void findAllEdittexts() {

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
                            Opener.NewsList(BaseActivity.this, Integer.parseInt(v.getTag().toString()), edittext.getText().toString());
//                            Toast.makeText(BaseActivity.this, "Testing testing: " + v.getTag(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
//                    array.put(edittext.getId(), edittext);
            }
        }


    }

}
