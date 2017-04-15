package com.app.sportcity.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.objects.ActiveMenuList;
import com.app.sportcity.objects.Category;
import com.app.sportcity.objects.Item;
import com.app.sportcity.objects.Menu;
import com.app.sportcity.objects.Post;
import com.app.sportcity.pushnotification.RegisterFirebaseToken;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.statics.StaticVariables;
import com.app.sportcity.utils.CommonMethods;
import com.app.sportcity.utils.MySharedPreference;
import com.app.sportcity.utils.Opener;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
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
    private MySharedPreference prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        prefs = new MySharedPreference(getApplicationContext());
        if(prefs.getStringValues(CommonMethods.TEMP_MAIL)==""){
            prefs.setKeyValues(CommonMethods.TEMP_MAIL, CommonMethods.getRandomMail());
        }
//        if(prefs.getStringValues(CommonMethods.FIREBASE_TOKEN)=="") {
//            RegisterFirebaseToken registerFirebaseToken = new RegisterFirebaseToken(getApplicationContext());
//            registerFirebaseToken.tokenRequestAndRegister();
//        }

//        getMenu();

        getLatestPost();

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

                Opener.BaseActivity(SplashScreen.this);
                finish();

//                if (hasError) {
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
//                            builder.setTitle("Error")
//                                    .setCancelable(false)
//                                    .setMessage(errorMsg)
//                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                            finish();
//                                        }
//                                    });
//                            AlertDialog dialog = builder.create();
//                            dialog.show();
//                        }
//                    });
//                } else {
//                    Opener.BaseActivity(SplashScreen.this);
//                    finish();
//                }
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

                if (t instanceof IOException) {
                    errorMsg = "No network or internet connection. Please check your connection and try again.";
                } else {
                    errorMsg = t.getMessage();
                }
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
            if (item.getObject().equals("category")) {
//            if (!item.getType().equals("custom")) {
                StaticVariables.ActiveMenuList.list.add(item);
            }
        }
        isCompleted = true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void getLatestPost() {
        apiCall = RetrofitSingleton.getApiCalls();
        Call<List<Post>> posts = apiCall.getLatestPosts();
        posts.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, final Response<List<Post>> response) {
                System.out.println("Response size:" + response.body().size());
                StaticVariables.news = response.body();

//                updateFeaturedImage();

                Call<List<Category>> categories = apiCall.getAllCategories();
                categories.enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> catResponse) {
                        StaticVariables.categories = catResponse.body();
//                        updateCategoryInPost(response.body(), catResponse.body());
                        isCompleted = true;
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {
                        hasError = true;

                        if (t instanceof IOException) {
                            errorMsg = "No network or internet connection. Please check your connection and try again.";
                        } else {
                            errorMsg = t.getMessage();
                        }
                    }
                });


            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                hasError = true;

                if (t instanceof IOException) {
                    errorMsg = "No network or internet connection. Please check your connection and try again.";
                } else {
                    errorMsg = t.getMessage();
                }
            }
        });
    }

    private void updateFeaturedImage() {

        for(int i=0; i<StaticVariables.news.size(); i++) {
            String content = StaticVariables.news.get(i).getContent().getRendered();
            String[] temp = content.split("src=\"");
            if(temp.length>1){
                String temp1 = temp[1].split("\"")[0];
                StaticVariables.news.get(i).setImgUrl(temp1);
                System.out.println("Rabin is testing: "+ temp1);
            }
        }
    }

    private void updateCategoryInPost(List<Post> response, List<Category> catResponse) {

        for (int i = 0; i < response.size(); i++) {
            int catId = response.get(i).getCategories().get(0);
            for(int j = 0; j<catResponse.size(); j++){
                if(catResponse.get(j).getId()==catId){
                    StaticVariables.news.get(i).setCatName(catResponse.get(j).getName());
                }
            }
        }
    }

}

