package com.app.sportcity.statics;

import com.app.sportcity.objects.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rabinshrestha on 2/16/17.
 */

public class StaticVariables {

    /**
     * Is the active and showing menu items
     */
    public static class ActiveMenuList {
        public static List<Item> list = new ArrayList<>();

        public static void reset() {
            list = new ArrayList<>();
        }
    }

}
