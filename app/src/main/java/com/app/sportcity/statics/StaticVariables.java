package com.app.sportcity.statics;

import com.app.sportcity.objects.CartDetails;
import com.app.sportcity.objects.Category;
import com.app.sportcity.objects.Item;
import com.app.sportcity.objects.ItemsDetail;
import com.app.sportcity.objects.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rabinshrestha on 2/16/17.
 */

public class StaticVariables {

    public static String CART_ITEM = "cart_item";
    public static List<Category> categories=new ArrayList<>();

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

        public static boolean addItem(ItemsDetail item) {
            if(!isItemInCart(item)) {
                cartDetails.getItemsDetail().add(item);
                addTotalCountNAmount(item);
                return true;
            }
            return false;
        }

        private static boolean isItemInCart(ItemsDetail item){
            if(cartDetails!=null && cartDetails.getItemsDetail()!=null) {
                for (int i = 0; i < cartDetails.getItemsDetail().size(); i++) {
                    ItemsDetail tempItem = cartDetails.getItemsDetail().get(i);
                    if (tempItem.getItemId() == item.getItemId()) {
                        return true;
                    }
                }
            }
            return false;
        }

        private static void addTotalCountNAmount(ItemsDetail item) {
            float temp = cartDetails.getTotalAmount();
            temp += item.getItemTotal();
            cartDetails.setTotalAmount(temp);
            cartDetails.setTotalCount(cartDetails.getTotalCount()+1);
        }

        public static void deleteItem(ItemsDetail item){
            for(int i=0; i<cartDetails.getItemsDetail().size();i++){
                if(item.getItemId()==cartDetails.getItemsDetail().get(i).getItemId()){
                    cartDetails.getItemsDetail().remove(i);
                    subTotalCount(item);
                    System.out.println("Item deleted: "+item.getItemId()+" - "+item.getItemName()+ " Count: "+ cartDetails.getItemsDetail().size());
                }
            }
        }

        private static void subTotalCount(ItemsDetail item) {
            float temp = cartDetails.getTotalAmount();
            temp -= item.getItemTotal();
            cartDetails.setTotalAmount(temp);
            cartDetails.setTotalCount(cartDetails.getTotalCount()-1);
        }
    }

}
