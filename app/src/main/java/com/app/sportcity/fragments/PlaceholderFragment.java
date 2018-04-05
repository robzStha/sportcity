package com.app.sportcity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.adapters.NewsListAdapter;
import com.app.sportcity.objects.Category;
import com.app.sportcity.objects.NewsList;
import com.app.sportcity.objects.Post;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.statics.StaticVariables;
import com.app.sportcity.utils.DataFeeder;
import com.app.sportcity.utils.EndlessRecyclerOnScrollListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceholderFragment extends Fragment {
    private List<Post> newsTemp;
    RecyclerView rvNewsList;
    NewsListAdapter newsListAdapter;
    EndlessRecyclerOnScrollListener scrollListener;
    LinearLayout llProgressBar;

    Context mContext;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_CAT_ID = "cat_id";
    private ApiCalls apiCalls;
    private int nextCatId;
    private int catId;
    private boolean hasNext;

    public PlaceholderFragment() {
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

        catId = getArguments().getInt(ARG_CAT_ID);
        rvNewsList = (RecyclerView) rootView.findViewById(R.id.rv_cats);
        llProgressBar = (LinearLayout) rootView.findViewById(R.id.ll_progressbar);

        getPostFromCategory(catId);
        return rootView;
    }

    private void getPostFromCategory(int catId) {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("Loading news...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        apiCalls = RetrofitSingleton.getApiCalls();
        Call<List<Post>> posts = apiCalls.getPosts(catId);
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

        if (!nextLink.equals("")) {
            String temp = nextLink.substring(nextLink.indexOf("="));
            String tempArray[] = temp.split("&");
            System.out.println("ykanello Debug : " + tempArray[0]);
            tempArray[0] = tempArray[0].replaceAll("[^\\d.]", "");   // strip ; from nextcatID
            System.out.println("ykanello Debug : " + tempArray[0]);
	    //apparently the following statement does not remove the ; at the end of the digit. Replaced the ; in the temparray-0 definition above. 
            //nextCatId = Integer.parseInt(tempArray[0].substring(1));
            nextCatId = Integer.parseInt(tempArray[0]);
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
        llProgressBar.setVisibility(View.VISIBLE);
        Call<List<Post>> posts = apiCalls.getPostsNext(catId, current_page);
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
                llProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                llProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }
}
