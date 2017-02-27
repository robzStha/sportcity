package com.app.sportcity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sportcity.R;
import com.app.sportcity.adapters.NewsListAdapter;
import com.app.sportcity.objects.NewsList;
import com.app.sportcity.objects.Post;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.utils.DataFeeder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceholderFragment extends Fragment {
    private final List<NewsList> newsLists;
    private List<Post> news;
    RecyclerView rvNewsList;

    Context mContext;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_CAT_ID = "cat_id";

    public PlaceholderFragment() {
        Gson gson = new Gson();
        newsLists = gson.fromJson(DataFeeder.Categories.getNewsList(), new TypeToken<List<NewsList>>() {
        }.getType());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int catId) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CAT_ID, catId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category_news_list, container, false);

        int catId = getArguments().getInt(ARG_CAT_ID);
        rvNewsList = (RecyclerView) rootView.findViewById(R.id.rv_cats);
        getPostFromCategory(catId);
        return rootView;
    }

    private void getPostFromCategory(int catId) {final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("Loading news...");
        pd.show();
        ApiCalls apiCalls = RetrofitSingleton.getApiCalls();
        Call<List<Post>> posts = apiCalls.getPosts(catId);
        posts.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                System.out.println("Response size:" + response.body().size());

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

    private void populateNews(List<Post> news) {
        rvNewsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNewsList.setAdapter(new NewsListAdapter(mContext, news));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }
}