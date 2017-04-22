package com.app.sportcity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sportcity.R;
import com.app.sportcity.objects.Media;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by rabinshrestha on 3/15/17.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {

    Context context;
    List<Media> shopItems;

    public ImagesAdapter(Context context, List<Media> shopItems) {
        this.context = context;
        this.shopItems = shopItems;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_image_list, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        System.out.println("Image: "+shopItems.get(position).getMediaDetails().getSizes().getThumbnail().getSourceUrl());
        holder.tvImgTitle.setText(shopItems.get(position).getTitle().getRendered());
        Glide.with(context)
                .load(shopItems.get(position).getMediaDetails().getSizes().getThumbnail().getSourceUrl())
                .centerCrop()
                .into(holder.ivItem);

//        holder.root.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Position: "+position, Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return shopItems.size();
    }

    public void appendNewImages(List<Media> body) {
        shopItems.addAll(body);
        notifyDataSetChanged();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        View root;
        TextView tvImgTitle;
        ImageView ivAddToCart;
        ImageView ivItem;

        public ImageViewHolder(View itemView) {
            super(itemView);

            root = itemView.findViewById(R.id.cl_image);
            tvImgTitle = (TextView) itemView.findViewById(R.id.tv_img_title);
            ivAddToCart = (ImageView) itemView.findViewById(R.id.iv_add_to_cart);
            ivItem = (ImageView) itemView.findViewById(R.id.iv_item);
        }
    }


    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        GestureDetector gestureDetector;
        ImagesAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ImagesAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
