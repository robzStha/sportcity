package com.app.sportcity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sportcity.R;
import com.app.sportcity.adapters.NewsListAdapter;
import com.app.sportcity.objects.Post;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.utils.EndlessRecyclerOnScrollListener;

import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeNewsFragment extends Fragment {
    RecyclerView rvNewsList;
    NewsListAdapter newsListAdapter;
    EndlessRecyclerOnScrollListener scrollListener;

    Context mContext;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_CAT_ID = "cat_id";
    private ApiCalls apiCalls;
//    private int nextCatId;
//    private int catId;
    private boolean hasNext;
    private List<Post> newsTemp;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
//    public static HomeNewsFragment newInstance(int catId) {
//        HomeNewsFragment fragment = new HomeNewsFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_CAT_ID, catId);
//        fragment.setArguments(args);
//
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category_news_list, container, false);

//        catId = getArguments().getInt(ARG_CAT_ID);
        rvNewsList = (RecyclerView) rootView.findViewById(R.id.rv_cats);

        getLatestPost();
        return rootView;
    }

    private void getLatestPost() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("Loading news...");
        pd.show();
        apiCalls = RetrofitSingleton.getApiCalls();
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
                System.out.println("Next linkss : " + temp + " Split: " + string[0] + " : " + string[1]);
                if (string[1].equals("rel=\"next\"")) {
                    System.out.println("Next linkss : next: " + string[1]);
                    nextLink = string[0];
                }
//                    nextLink = string[0];
                populateNews(response.body(), nextLink);
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                pd.dismiss();
            }
        });

    }

    private void populateNews(final List<Post> news, final String nextLink) {
        System.out.println("Next link : " + nextLink);
        if (!nextLink.equals("")) {
            String temp = nextLink.substring(nextLink.indexOf("="));
            String tempArray[] = temp.split("&");
//            nextCatId = Integer.parseInt(tempArray[0].substring(1));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            rvNewsList.setLayoutManager(linearLayoutManager);
            newsListAdapter = new NewsListAdapter(mContext, news);
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
        }
        rvNewsList.setAdapter(newsListAdapter);
    }

    private void loadMoreFromAPI(int current_page) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading more news");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Call<List<Post>> posts = apiCalls.getLatestPostsNext(current_page);
        posts.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                System.out.println("Response size:" + response.body().size());
                Headers headers = response.headers();
                String temp = headers.get("Link").replace("<", "");
                temp = temp.replace(">", "");
                String string[] = temp.split(" ");
//                String nextLink = "";
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
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }



}
