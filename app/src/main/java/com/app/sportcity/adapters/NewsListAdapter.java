package com.app.sportcity.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.app.sportcity.objects.NewsList;
import com.app.sportcity.objects.Post;
import com.app.sportcity.utils.Opener;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {
    LayoutInflater inflater;

    List<Post> newsLists;
    Context mContext;

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
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "Clicked on news: " + newsLists.get(position).getNewsTile(), Toast.LENGTH_SHORT).show();
//                Opener.NewsDetails((Activity)mContext, newsLists.get(position));
            }
        });
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