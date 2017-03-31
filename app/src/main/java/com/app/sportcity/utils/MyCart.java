package com.app.sportcity.utils;

import android.content.Context;
import android.widget.Toast;

import com.app.sportcity.objects.CartDetails;
import com.app.sportcity.objects.Img;
import com.app.sportcity.objects.ItemsDetail;
import com.app.sportcity.statics.StaticVariables;

import java.util.ArrayList;

import static com.app.sportcity.statics.StaticVariables.Cart.cartDetails;

/**
 * Created by bugatti on 22/01/17.
 */

public class MyCart {

//    Context context;
    private static MyCart myCartInstance;

    private MyCart(){}

    public static MyCart getInstance () {
        if(myCartInstance==null){
            myCartInstance = new MyCart();
        }
        return myCartInstance;
    }

    public boolean addItemToCart(ItemsDetail item) {
        return StaticVariables.Cart.addItem(item);
    }

    private int getItemCount() {
        return StaticVariables.Cart.cartDetails.getTotalCount();
    }

}
