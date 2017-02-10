package com.app.sportcity.server_protocols;

import com.app.sportcity.objects.Category;
import com.app.sportcity.objects.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("categories?parent=0&per_page=15")
    Call<List<Category>> getCategories();

    @GET("posts")
    Call<List<Post>> getPosts(@Query("categories") int id);

}
