package com.app.sportcity.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStructure;
import android.widget.TextView;

import com.app.sportcity.R;
import com.app.sportcity.fragments.PlaceholderFragment;
import com.app.sportcity.statics.StaticVariables;
import com.app.sportcity.utils.MyMenuItemStuffListener;
import com.app.sportcity.utils.Opener;

public class NewsList extends AppCompatActivity {
    int catId = -1;
    private TextView ui_hot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            catId = bundle.getInt("catId");
            getSupportActionBar().setTitle(bundle.getString("catTitle"));
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PlaceholderFragment hello = PlaceholderFragment.newInstance(catId);
        fragmentTransaction.add(R.id.activity_news_list, hello, "HELLO");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                Opener.CartList(NewsList.this);
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
