package com.app.sportcity.statics;

import com.app.sportcity.objects.CartDetails;
import com.app.sportcity.objects.Category;
import com.app.sportcity.objects.Item;
import com.app.sportcity.objects.ItemDetail;
import com.app.sportcity.objects.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rabinshrestha on 2/16/17.
 */

public class StaticVariables {

    public static String CART_ITEM = "cart_item";
    public static List<Category> categories = new ArrayList<>();

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

    public static CartDetails cartDetails = new CartDetails();
    public static CartDetails downloadedItems = new CartDetails();
    public static CartDetails downloadIssueItems = new CartDetails();

    public static class Cart {

        public static void addDownloadedItems(ItemDetail item) {
            downloadedItems.getItemDetail().add(item);
        }
         public static void addDownloadIssueItem(ItemDetail item) {
             downloadIssueItems.getItemDetail().add(item);
        }

        public static void resetDownloadedItems(){
            downloadedItems = new CartDetails();
        }
        public static void resetDownloadIssueItems(){
            downloadIssueItems = new CartDetails();
        }

        public static void reset() {
            cartDetails = new CartDetails();
        }

        public static boolean addItem(ItemDetail item) {
            if (!isItemInCart(item)) {
                cartDetails.getItemDetail().add(item);
                addTotalCountNAmount(item);
                return true;
            }
            return false;
        }

        private static boolean isItemInCart(ItemDetail item) {
            if (cartDetails != null && cartDetails.getItemDetail() != null) {
                for (int i = 0; i < cartDetails.getItemDetail().size(); i++) {
                    ItemDetail tempItem = cartDetails.getItemDetail().get(i);
                    if (tempItem.getItemId() == item.getItemId()) {
                        return true;
                    }
                }
            }
            return false;
        }

        private static void addTotalCountNAmount(ItemDetail item) {
            float temp = cartDetails.getTotalAmount();
            temp += item.getItemTotal();
            cartDetails.setTotalAmount(temp);
            cartDetails.setTotalCount(cartDetails.getTotalCount() + 1);
        }

        public static void deleteItem(ItemDetail item) {
            for (int i = 0; i < cartDetails.getItemDetail().size(); i++) {
                if (item.getItemId() == cartDetails.getItemDetail().get(i).getItemId()) {
                    cartDetails.getItemDetail().remove(i);
                    subTotalCount(item);
                    System.out.println("Item deleted: " + item.getItemId() + " - " + item.getItemName() + " Count: " + cartDetails.getItemDetail().size());
                }
            }
        }

        private static void subTotalCount(ItemDetail item) {
            float temp = cartDetails.getTotalAmount();
            temp -= item.getItemTotal();
            cartDetails.setTotalAmount(temp);
            cartDetails.setTotalCount(cartDetails.getTotalCount() - 1);
        }
    }

}
