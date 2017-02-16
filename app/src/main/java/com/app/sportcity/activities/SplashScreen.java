package com.app.sportcity.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.objects.ActiveMenuList;
import com.app.sportcity.objects.Item;
import com.app.sportcity.objects.Menu;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.statics.StaticVariables;
import com.app.sportcity.utils.Opener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar;
    int progress = 0;
    private ApiCalls apiCall;
    int activeMenuId = -1;
    boolean isCompleted = false;
    boolean hasError = false;
    String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        getMenu();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress <= 1000) {
                    try {
                        Thread.sleep(7);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                System.out.println("Progress: " + progress);
                                progressBar.setProgress(progress++);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                while (!isCompleted && hasError == false) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                if(hasError){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                            builder.setTitle("Error")
                                    .setCancelable(false)
                                    .setMessage(errorMsg+"\nMay be internet connection issue. Please check if device is connected to internet.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }else {
                    Opener.BaseActivity(SplashScreen.this);
                    finish();
                }
            }
        }).start();
    }

    private void getMenu() {

        apiCall = RetrofitSingleton.getApiCalls();
        Call<List<Menu>> menus = apiCall.getMenus();
        menus.enqueue(new Callback<List<Menu>>() {
            @Override
            public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {
                getActiveMenuId(response.body());
            }

            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {
                hasError = true;
                errorMsg = t.getMessage();
            }
        });

    }

    private void getActiveMenuId(List<Menu> menus) {
        activeMenuId = menus.get(0).getID();
        getActiveMenuList(activeMenuId);
    }

    private void getActiveMenuList(int activeMenuId) {

        Call<ActiveMenuList> activeMenuList = apiCall.getActiveMenuList(activeMenuId);
        activeMenuList.enqueue(new Callback<ActiveMenuList>() {
            @Override
            public void onResponse(Call<ActiveMenuList> call, Response<ActiveMenuList> response) {
                getMenuWithoutCustomType(response.body().getItems());
            }

            @Override
            public void onFailure(Call<ActiveMenuList> call, Throwable t) {
                hasError = true;
                errorMsg = t.getMessage();
            }
        });
    }

    private void getMenuWithoutCustomType(List<Item> menuItems) {
        if (StaticVariables.ActiveMenuList.list.size() > 0) {
            StaticVariables.ActiveMenuList.reset();
        }
        for (Item item : menuItems) {
            if (!item.getType().equals("custom")) {
                StaticVariables.ActiveMenuList.list.add(item);
            }
        }
        isCompleted = true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}

