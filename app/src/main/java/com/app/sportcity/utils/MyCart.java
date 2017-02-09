package com.app.sportcity.utils;

import android.content.Context;
import android.widget.Toast;

import com.app.sportcity.objects.Img;

import java.util.ArrayList;

/**
 * Created by bugatti on 22/01/17.
 */

public class MyCart {

    Context context;

    public MyCart(Context context) {
        this.context = context;
    }

    private static ArrayList<Img> imgs = new ArrayList<>();

    public int addItemToCart(Img item) {
        if (item.getIsPurchased() == "true" || imgs.contains(item)) {
            Toast.makeText(context, "This item has already been purchased.", Toast.LENGTH_LONG).show();
        } else {
            imgs.add(item);
        }
        return getItemCount();
    }

    private int getItemCount() {
        return imgs.size();
    }

    public int deleteItem(String id) {
        for (Img img : imgs) {
            if (img.getImgId().equals(id)) {
                imgs.remove(img);
            }
        }
        return getItemCount();
    }

    public ArrayList<Img> getCartItems() {
        return imgs;
    }

    public float getTotal() {
        return 10 * imgs.size();
    }
}
