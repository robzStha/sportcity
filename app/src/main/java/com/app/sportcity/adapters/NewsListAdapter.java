package com.app.sportcity.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.activities.CategoryNewsList;
import com.app.sportcity.objects.Category;
import com.app.sportcity.objects.Media;
import com.app.sportcity.objects.NewsList;
import com.app.sportcity.objects.Post;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.statics.StaticVariables;
import com.app.sportcity.utils.CommonMethods;
import com.app.sportcity.utils.Opener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    LayoutInflater inflater;

    List<Post> newsLists;
    Context mContext;
    private ApiCalls apiCall;
    boolean hasNext = false;

    public NewsListAdapter(Context context, List<Post> newsLists) {
        this.mContext = context;
        this.newsLists = newsLists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        System.out.println("VIewType: " + viewType);
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.item_category_list, parent, false);
            return new NewsViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_cat_list_1, parent, false);
            return new NewsViewHolder1(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case 0:
                bindNewsViewHolder((NewsViewHolder) holder, position);
                break;
            case 1:
                bindNewsView1Holder((NewsViewHolder1) holder, position);
                break;
        }


    }

    private void bindNewsViewHolder(NewsViewHolder holder, final int position) {
        NewsViewHolder newsViewHolder = holder;
        newsViewHolder.tvTitle.setText(Html.fromHtml("<b>"+newsLists.get(position).getTitle().getRendered()+"</b>"));
        newsViewHolder.tvDesc.setText(Html.fromHtml(newsLists.get(position).getExcerpt().getRendered()));

//        convertTime(position);
        String elapsedTime = CommonMethods.timeElapsed(newsLists.get(position).getDate().replace("T", " "));
        newsViewHolder.tvDate.setText(elapsedTime);

        if (newsLists.get(position).getImgUrl() != null) {
            newsViewHolder.ivFeatImg.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(newsLists.get(position).getImgUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivFeatImg);
//            imageFinder(newsLists.get(position).getImgUrl(), holder.ivFeatImg);
        } else
            newsViewHolder.ivFeatImg.setVisibility(View.GONE);

        newsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opener.NewsDetails((Activity) mContext, newsLists.get(position));
            }
        });
    }

//    private String convertTime(int position) {
//        String sDate = newsLists.get(position).getDate().replace("T", " ");
//        return timeElapsed(sDate);
//    }


    private String getImageUrlFromPost(String rendered) {

        String[] temp = rendered.split("src=\"");
        if (temp.length > 1) {
            String href = temp[1];
            System.out.println("Rabin is testingssss: " + href);
        }

        return null;
    }

    private void bindNewsView1Holder(NewsViewHolder1 holder, final int position) {
        NewsViewHolder1 newsViewHolder = holder;
        if (newsLists.get(position).getCatName() != null) {
            newsViewHolder.tvCatTitle.setText(newsLists.get(position).getCatName());
        } else
            newsViewHolder.tvCatTitle.setText(newsLists.get(position).getCategories().get(0) + "");
        newsViewHolder.tvNewsTitle.setText(Html.fromHtml(newsLists.get(position).getTitle().getRendered()));
//        convertTime(position, newsViewHolder.tvDate);
        String elapsedTime = CommonMethods.timeElapsed(newsLists.get(position).getDate().replace("T", " "));
        newsViewHolder.tvDate.setText(elapsedTime);
        newsViewHolder.ivFeatImg.setVisibility(View.VISIBLE);
        if (newsLists.get(position).getImgUrl() != null) {
            newsViewHolder.ivFeatImg.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(newsLists.get(position).getImgUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivFeatImg);
        } else
            newsViewHolder.ivFeatImg.setVisibility(View.GONE);
        newsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opener.NewsDetails((Activity) mContext, newsLists.get(position));
            }
        });
    }

    private void imageFinder(int featuredImgId, final ImageView imageView) {
        apiCall = RetrofitSingleton.getApiCalls();
        Call<Media> mediaCall = apiCall.getMedia(featuredImgId);
        mediaCall.enqueue(new Callback<Media>() {
            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                Media media = response.body();
                String temp = "";
                try {
                    temp = media.getMediaDetails().getSizes().getMedium().getSourceUrl();
                    if (!temp.equals("")) {
                        imageView.setVisibility(View.GONE);
                        Glide.with(mContext)
                                .load(response.body().getMediaDetails().getSizes().getMedium().getSourceUrl())
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView);
                    } else {
                        imageView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    imageView.setVisibility(View.GONE);
                }
                System.out.println("Image url: " + temp);
            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
//        if (position % 3 == 0) {
//            if (newsLists.get(position).getImgUrl() != null) {
//                viewType = 1;
//            } else {
//                viewType = 0;
//            }
//        } else
//            viewType = 0;

        return viewType;
    }

    @Override
    public int getItemCount() {
        return newsLists.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDesc, tvDate;
        ImageView ivFeatImg;
        View mView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_news_date);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            ivFeatImg = (ImageView) itemView.findViewById(R.id.iv_feat_img);
            mView = itemView.findViewById(R.id.ll_holder);
        }
    }

    public class NewsViewHolder1 extends RecyclerView.ViewHolder {

        TextView tvCatTitle, tvDate, tvNewsTitle;
        ImageView ivFeatImg;
        View mView;

        public NewsViewHolder1(View itemView) {
            super(itemView);
            tvCatTitle = (TextView) itemView.findViewById(R.id.tv_cat_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_news_date);
            tvNewsTitle = (TextView) itemView.findViewById(R.id.tv_news_title);
            ivFeatImg = (ImageView) itemView.findViewById(R.id.iv_feat_img);
            mView = itemView.findViewById(R.id.ll_cat_list_1);
        }
    }

    public void appendNewNews(List<Post> news) {
        newsLists.addAll(news);
        updateFeaturedImage();
        updateCategoryInPost();
        notifyDataSetChanged();
        Log.d("Next link : news size ", newsLists.size() + "");
    }

    private void updateFeaturedImage() {

        for (int i = 0; i < newsLists.size(); i++) {
            if (newsLists.get(i).getImgUrl() == null) {
                String content = newsLists.get(i).getContent().getRendered();
                String[] temp = content.split("src=\"");
                if (temp.length > 1) {
                    String temp1 = temp[1].split("\"")[0];
                    newsLists.get(i).setImgUrl(temp1);
                    System.out.println("Rabin is testing: " + temp1);
                }
            }
        }
    }

    private void updateCategoryInPost() {

        for (int i = 0; i < newsLists.size(); i++) {
            if (newsLists.get(i).getCatName() == null) {
                int catId = newsLists.get(i).getCategories().get(0);
                for (int j = 0; j < StaticVariables.categories.size(); j++) {
                    if (StaticVariables.categories.get(j).getId() == catId) {
                        newsLists.get(i).setCatName(StaticVariables.categories.get(j).getName());
                    }
                }
            }
        }
    }

}