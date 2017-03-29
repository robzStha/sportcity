package com.app.sportcity.statics;

import com.app.sportcity.objects.CartDetails;
import com.app.sportcity.objects.Category;
import com.app.sportcity.objects.Item;
import com.app.sportcity.objects.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rabinshrestha on 2/16/17.
 */

public class StaticVariables {

    public static List<Category> categories;

    public static void resetCat() {
        categories = new ArrayList<>();
    }

    /**
     * Is the active and showing menu items
     */
    public static class ActiveMenuList {
        public static List<Item> list = new ArrayList<>();

        public static void reset() {
            list = new ArrayList<>();
        }
    }

    public static List<Post> news = new ArrayList<>();

    public static void reset() {
        news = new ArrayList<>();
    }

    public static class Cart {
        public static CartDetails cartDetails = new CartDetails();

        public static void reset() {
            cartDetails = new CartDetails();
        }


//    public boolean isItemPurchased(ItemsDetail item){
//        for(int i=0; i<itemsDetail.size(); i++){
//            ItemsDetail tempItem = itemsDetail.get(i);
//            if(tempItem.getItemId()==item.getItemId()){
//                return true;
//            }else return false;
//        }
//        return false;
//    }
//
//    public void addItem(ItemsDetail item) {
//        if(!isItemPurchased(item)){
//            itemsDetail = getItemsDetail();
//            itemsDetail.add(item);
//        }
//    }
    }

}
