package com.app.sportcity.pushnotification;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.utils.CommonMethods;
import com.app.sportcity.utils.MySharedPreference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nabin on 9/26/16.
 */

public class RegisterFirebaseToken {
    private static final String TAG = "Rabin is testing";// = RegisterFirebaseToken.class.getSimpleName();
    private Context mContext;
    private String notificationToken;
    private MySharedPreference prefs;

    public RegisterFirebaseToken(Context context) {
        this.mContext = context;
        this.prefs = new MySharedPreference(context);

    }

    public void tokenRequestAndRegister() {
//        FirebaseMessaging.getInstance().subscribeToTopic("news");
        notificationToken = FirebaseInstanceId.getInstance().getToken();
        if (notificationToken != null && !notificationToken.isEmpty()) {
            Log.d(TAG, "notificationTokenReadyToRegister => " + notificationToken);
            String temp = prefs.getStringValues(CommonMethods.FIREBASE_TOKEN);
//            if(!temp.equals(notificationToken)) {
            prefs.setKeyValues(CommonMethods.FIREBASE_TOKEN, notificationToken);
            new registerFirebaseTokenToServer().execute();
//            }

        } //else {
//            tokenRequestAndRegister();
//        }
    }

    public class registerFirebaseTokenToServer extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
        }

        @Override
        protected String doInBackground(Void... param) {
            ApiCalls apiService = RetrofitSingleton.getApiCalls();
            String randomMail;
            if (!prefs.getStringValues(CommonMethods.TEMP_MAIL).equals("")) {
                randomMail = prefs.getStringValues(CommonMethods.TEMP_MAIL);
            } else {
                randomMail = CommonMethods.getRandomMail();
                prefs.setKeyValues(CommonMethods.TEMP_MAIL, randomMail);
            }
            System.out.println("Random mail: "+randomMail);
            Call<ResponseToken> call = apiService.setDeviceToken("android", randomMail, prefs.getStringValues(CommonMethods.FIREBASE_TOKEN));
            call.enqueue(new Callback<ResponseToken>() {
                @Override
                public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                    try {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "TokenRegister " + response.body().SuccessMessage);
                            System.out.println("Token is registered; " + response.body().toString());
//                            if (response.body().code.equalsIgnoreCase("0001")) {
//                                Log.d(TAG, "TokenRegister " + response.body().msg);
//                            } else {
//                                Log.d(TAG, "TokenRegister " + response.body().msg);
//                            }
                        } else {
                            Log.d(TAG, response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseToken> call, Throwable t) {
                    t.printStackTrace();
                    Log.d(TAG, t.getMessage());
                }
            });

            return null;
        }
    }
}