package com.app.sportcity.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sportcity.R;

/**
 * Created by bugatti on 17/11/16.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivCatImage;
    public TextView tvCatTitle;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        this.ivCatImage = (ImageView) itemView.findViewById(R.id.iv_catImage);
        this.tvCatTitle = (TextView) itemView.findViewById(R.id.tv_catTitle);

    }


}
