package com.app.sportcity.utils;

import android.content.Context;
import android.widget.Toast;

import com.app.sportcity.objects.CartDetails;
import com.app.sportcity.objects.Img;
import com.app.sportcity.objects.ItemsDetail;

import java.util.ArrayList;

/**
 * Created by bugatti on 22/01/17.
 */

public class MyCart {

    Context context;
    private static MyCart myCartInstance;
    private static CartDetails cartDetails;

    private MyCart(){}

    public MyCart getInstance () {
        if(myCartInstance==null){
            myCartInstance = new MyCart();
        }
        return myCartInstance;
    }

    public void init(Context context, CartDetails cartDetails){
        this.cartDetails = cartDetails;
        this.context = context;
    }
    private ArrayList<ItemsDetail> itemsDetails = new ArrayList<>();

    public int addItemToCart(ItemsDetail item) {
        cartDetails.addItem(item);

        return getItemCount();
    }

    private int getItemCount() {
        return cartDetails.getItemsDetail().size();
    }

//    public int deleteItem(String id) {
//        for (Img img : imgs) {
//            if (img.getImgId().equals(id)) {
//                imgs.remove(img);
//            }
//        }
//        return getItemCount();
//    }
//
//    public ArrayList<Img> getCartItems() {
//        return imgs;
//    }
//
//    public float getTotal() {
//        return 10 * imgs.size();
//    }
}
