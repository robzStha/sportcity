package com.app.sportcity.server_protocols;

import com.app.sportcity.objects.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by bugatti on 22/11/16.
 */

public interface ApiCalls {
//
//    @FormUrlEncoded
//    @POST("UserRegistration")
//    Call<>

//    @GET("menu_category")
//    Call<CategoryNewsListing> getMenuCategories();
//
//
//    @GET("home")
//    Call<HomeResponse> getHomeResponse();
//
//    @GET("category/{id}")
//    Call<MenuCategory> getCategoriesNewsById(@Path("id") long id);

    @GET("categories?parent=0")
    Call<List<Category>> getCategories();

}
