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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.sportcity.R;
import com.app.sportcity.adapters.NewsListAdapter;
import com.app.sportcity.objects.Post;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.statics.StaticVariables;
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
    LinearLayout llProgressBar;
    private View rootView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category_news_list, container, false);
        rvNewsList = (RecyclerView) rootView.findViewById(R.id.rv_cats);
        llProgressBar = (LinearLayout) rootView.findViewById(R.id.ll_progressbar);
        getLatestPost();
        return rootView;
    }

    private void getLatestPost() {

        apiCalls = RetrofitSingleton.getApiCalls();
        if (StaticVariables.news.size() > 0) {
            newsTemp = StaticVariables.news;
            populateNews(newsTemp);
            StaticVariables.reset();
        } else {

            final ProgressDialog pd = new ProgressDialog(mContext);
            pd.setMessage("Loading news...");
            pd.show();
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

    }

    private void populateNews(final List<Post> news) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvNewsList.setLayoutManager(linearLayoutManager);
        newsListAdapter = new NewsListAdapter(mContext, news);
        scrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (hasNext)
                    loadMoreFromAPI(current_page);
            }
        };

        try {
            rvNewsList.addOnScrollListener(scrollListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rvNewsList.setAdapter(newsListAdapter);
    }

    private void loadMoreFromAPI(int current_page) {
        llProgressBar.setVisibility(View.VISIBLE);
        Call<List<Post>> posts = apiCalls.getLatestPostsNext(current_page);
        posts.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                System.out.println("Response size:" + response.body().size());
                Headers headers = response.headers();
                String temp = headers.get("Link").replace("<", "");
                temp = temp.replace(">", "");
                String string[] = temp.split(" ");
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
//                progressDialog.dismiss();
                llProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                llProgressBar.setVisibility(View.GONE);
//                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

}
