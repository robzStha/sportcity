package com.app.sportcity.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sportcity.R;
import com.app.sportcity.objects.Category;
import com.app.sportcity.objects.CategorySer;
import com.app.sportcity.utils.Opener;
import com.app.sportcity.view_holder.CategoryViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bugatti on 18/11/16.
 */

public class CatAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    List<Category> categories = new ArrayList<>();
    Context mContext;

    public CatAdapter(List<Category> categories, Context mContext) {
        this.categories = categories;
        this.mContext = mContext;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_cats, null, false);
//        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(parent);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {

        final Category category = categories.get(position);
        holder.tvCatTitle.setText(category.getName());
        holder.tvCatTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opener.CategoryNewsListing((Activity) mContext, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
