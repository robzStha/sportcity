package com.app.sportcity.utils;

import android.content.Context;
import android.widget.Toast;

import com.app.sportcity.objects.Img;

import java.util.ArrayList;

/**
 * Created by bugatti on 22/01/17.
 */
public class Cart {
    private static Cart ourInstance;

    Context context;

    public static Cart getInstance() {
        if (ourInstance == null) {
            ourInstance = new Cart();
        }
        return ourInstance;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    // initialization of private constructor so no instance can be made outside the class
    private Cart() {
    }

    private static ArrayList<Img> imgs = new ArrayList<>();

    public int addItemToCart(Img item) {
        if (hasItem(item)) {
            Toast.makeText(context, "This item is already in your cart", Toast.LENGTH_LONG).show();
        } else if (item.getIsPurchased().equals("true")) {
            Toast.makeText(context, "You have already purchased this item", Toast.LENGTH_LONG).show();
        } else {
            imgs.add(item);
            Toast.makeText(context, "Successfully added to your cart.", Toast.LENGTH_LONG).show();
        }
        return getItemCount();
    }

    public boolean hasItem(Img item) {
        for (Img img : imgs) {
            if (item.getImgId().equals(img.getImgId())) {
                return true;
            }
        }
        return false;
    }


    public int getItemCount() {
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
