package com.app.sportcity.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.activities.CategoryNewsList;
import com.app.sportcity.objects.Media;
import com.app.sportcity.objects.NewsList;
import com.app.sportcity.objects.Post;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.utils.Opener;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {
    LayoutInflater inflater;

    List<Post> newsLists;
    Context mContext;
    private ApiCalls apiCall;

    public NewsListAdapter(Context context, List<Post> newsLists) {
        this.mContext = context;
        this.newsLists = newsLists;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_category_list, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, final int position) {
        holder.tvTitle.setText(Html.fromHtml(newsLists.get(position).getTitle().getRendered()));
        holder.tvDesc.setText(Html.fromHtml(newsLists.get(position).getExcerpt().getRendered()));
        imageFinder(newsLists.get(position).getFeaturedMedia(), holder.ivFeatImg);
//        holder.ivFeatImg.setImageBitmap(newsLists.get(position).getFeaturedMedia());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "Clicked on news: " + newsLists.get(position).getNewsTile(), Toast.LENGTH_SHORT).show();
//                Opener.NewsDetails((Activity)mContext, newsLists.get(position));
            }
        });
    }


    private void imageFinder(int featuredImgId, final ImageView imageView){
        apiCall = RetrofitSingleton.getApiCalls();
        Call<Media> mediaCall = apiCall.getMedia(featuredImgId);
        mediaCall.enqueue(new Callback<Media>() {
            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                Media media = response.body();
                String temp="";
                try {
                    temp = media.getMediaDetails().getSizes().getMedium().getSourceUrl();
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("Image url: "+temp);
                if(!temp.equals("")) {
                Glide.with(mContext)
                        .load(response.body().getMediaDetails().getSizes().getMedium().getSourceUrl())
                        .centerCrop()
                        .placeholder(R.drawable.images)
                        .into(imageView);
                }else{
                    Glide.with(mContext)
                            .load(R.drawable.images)
                            .centerCrop()
                            .into(imageView);
                }
            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {

            }
        });

    }

    private class ImageFinder extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return newsLists.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDesc;
        ImageView ivFeatImg;
        View mView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            ivFeatImg = (ImageView) itemView.findViewById(R.id.iv_feat_img);
            mView = itemView.findViewById(R.id.ll_holder);
        }
    }
}