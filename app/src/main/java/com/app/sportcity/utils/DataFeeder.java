package com.app.sportcity.utils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.sportcity.R;
import com.app.sportcity.fragments.MyDialogFragment;
import com.app.sportcity.objects.Img;

import java.util.ArrayList;

/**
 * Created by bugatti on 09/12/16.
 */

public class DataFeeder {

    public static class Categories{

        public static String getNewsList(){
            return "[" +
                    "    {" +
                    "      \"news_title\": \"this is title\"," +
                    "      \"news_desc\": \"description of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having so\"," +
                    "      \"cat_id\": \"1\"," +
                    "      \"cat_name\": \"Ramos\"," +
                    "      \"news_id\": \"2\"," +
                    "      \"is_fav\": \"0\"," +
                    "      \"published_date\": \"JAN 06\"," +
                    "      \"feat_img_url\": \"this/is/feat.img\"," +
                    "      \"img\": [" +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }" +
                    "      ]," +
                    "      \"is_fav\": \"true\"" +
                    "    }," +
                    "    {" +
                    "      \"news_title\": \"this is title\"," +
                    "      \"news_desc\": \"description of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having so\"," +
                    "      \"cat_id\": \"2\"," +
                    "      \"cat_name\": \"Teresa\"," +
                    "      \"news_id\": \"5\"," +
                    "      \"is_fav\": \"0\"," +
                    "      \"published_date\": \"JAN 06\"," +
                    "      \"feat_img_url\": \"this/is/feat.img\"," +
                    "      \"img\": [" +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }" +
                    "      ]," +
                    "      \"is_fav\": \"true\"" +
                    "    }," +
                    "    {" +
                    "      \"news_title\": \"this is title\"," +
                    "      \"news_desc\": \"description of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having so\"," +
                    "      \"cat_id\": \"1\"," +
                    "      \"cat_name\": \"Ramos\"," +
                    "      \"news_id\": \"5\"," +
                    "      \"is_fav\": \"0\"," +
                    "      \"published_date\": \"JAN 06\"," +
                    "      \"feat_img_url\": \"this/is/feat.img\"," +
                    "      \"img\": [" +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }" +
                    "      ]," +
                    "      \"is_fav\": \"true\"" +
                    "    }," +
                    "    {" +
                    "      \"news_title\": \"this is the longest title ever to see in this field by any one of you\"," +
                    "      \"news_desc\": \"description of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having so\"," +
                    "      \"cat_id\": \"1\"," +
                    "      \"cat_name\": \"Sheena\"," +
                    "      \"news_id\": \"2\"," +
                    "      \"is_fav\": \"1\"," +
                    "      \"published_date\": \"JAN 06\"," +
                    "      \"feat_img_url\": \"this/is/feat.img\"," +
                    "      \"img\": [" +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }" +
                    "      ]," +
                    "      \"is_fav\": \"true\"" +
                    "    }," +
                    "    {" +
                    "      \"news_title\": \"this is title\"," +
                    "      \"news_desc\": \"description of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having so\"," +
                    "      \"cat_id\": \"1\"," +
                    "      \"cat_name\": \"Atkinson\"," +
                    "      \"news_id\": \"5\"," +
                    "      \"is_fav\": \"0\"," +
                    "      \"published_date\": \"JAN 06\"," +
                    "      \"feat_img_url\": \"this/is/feat.img\"," +
                    "      \"img\": [" +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }" +
                    "      ]," +
                    "      \"is_fav\": \"true\"" +
                    "    }," +
                    "    {" +
                    "      \"news_title\": \"this is title\"," +
                    "      \"news_desc\": \"description of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having so\"," +
                    "      \"cat_id\": \"4\"," +
                    "      \"cat_name\": \"Miles\"," +
                    "      \"news_id\": \"5\"," +
                    "      \"is_fav\": \"0\"," +
                    "      \"published_date\": \"JAN 06\"," +
                    "      \"feat_img_url\": \"this/is/feat.img\"," +
                    "      \"img\": [" +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }" +
                    "      ]," +
                    "      \"is_fav\": \"true\"" +
                    "    }," +
                    "    {" +
                    "      \"news_title\": \"this is title\"," +
                    "      \"news_desc\": \"description of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having so\"," +
                    "      \"cat_id\": \"5\"," +
                    "      \"cat_name\": \"Harrel\"," +
                    "      \"news_id\": \"2\"," +
                    "      \"is_fav\": \"1\"," +
                    "      \"published_date\": \"JAN 06\"," +
                    "      \"feat_img_url\": \"this/is/feat.img\"," +
                    "      \"img\": [" +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }" +
                    "      ]," +
                    "      \"is_fav\": \"true\"" +
                    "    }," +
                    "    {" +
                    "      \"news_title\": \"this is title\"," +
                    "      \"news_desc\": \"description of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having sodescription of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having sodescription of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having sodescription of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having so\"," +
                    "      \"cat_id\": \"1\"," +
                    "      \"cat_name\": \"Ramos\"," +
                    "      \"news_id\": \"5\"," +
                    "      \"is_fav\": \"0\"," +
                    "      \"published_date\": \"JAN 06\"," +
                    "      \"feat_img_url\": \"this/is/feat.img\"," +
                    "      \"img\": [" +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }" +
                    "      ]," +
                    "      \"is_fav\": \"true\"" +
                    "    }," +
                    "    {" +
                    "      \"news_title\": \"this is title\"," +
                    "      \"news_desc\": \"description of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having so\"," +
                    "      \"cat_id\": \"6\"," +
                    "      \"cat_name\": \"Doyle\"," +
                    "      \"news_id\": \"5\"," +
                    "      \"is_fav\": \"0\"," +
                    "      \"published_date\": \"JAN 06\"," +
                    "      \"feat_img_url\": \"this/is/feat.img\"," +
                    "      \"img\": [" +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }" +
                    "      ]," +
                    "      \"is_fav\": \"true\"" +
                    "    }," +
                    "    {" +
                    "      \"news_title\": \"this is title\"," +
                    "      \"news_desc\": \"description of the news and having fun with this news section and this is it so lets chat next time and you are drunk with everybody else. Goodluck having so\"," +
                    "      \"cat_id\": \"3\"," +
                    "      \"cat_name\": \"Sheena\"," +
                    "      \"news_id\": \"4\"," +
                    "      \"is_fav\": \"0\"," +
                    "      \"published_date\": \"JAN 06\"," +
                    "      \"feat_img_url\": \"this/is/feat.img\"," +
                    "      \"img\": [" +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/sale.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"10\"" +
                    "        }," +
                    "        {" +
                    "          \"img_url\": \"this/is/test.img\"," +
                    "          \"img_price\": \"100\"," +
                    "          \"img_id\": \"11\"" +
                    "        }" +
                    "      ]," +
                    "      \"is_fav\": \"true\"" +
                    "    }" +
                    "  ]";
        }

        public static String getCategories(){
            return "[" +
                    "  {" +
                    "    \"catId\": \"582dd4eee6198f464c71d7b1\"," +
                    "    \"isActive\": false," +
                    "    \"imgUrl\": \"https://alisahcreative.files.wordpress.com/2014/05/entertainment-1.jpg\"," +
                    "    \"catTitle\": \"Ramos\"" +
                    "  }," +
                    "  {" +
                    "    \"catId\": \"582dd4eea26b86afd5e2cb20\"," +
                    "    \"isActive\": false," +
                    "    \"imgUrl\": \"http://pinxmas.com/wp-content/uploads/2013/11/holidaychristmasxmaswallpapers-1385203254gkn48.jpg\"," +
                    "    \"catTitle\": \"Teresa\"" +
                    "  }," +
                    "  {" +
                    "    \"catId\": \"582dd4ee5f30dd5503178d49\"," +
                    "    \"isActive\": false," +
                    "    \"imgUrl\": \"http://www.roiinvesting.com/wp-content/uploads/2015/11/Beautiful-Beach-Holidays.jpg\"," +
                    "    \"catTitle\": \"Sheena\"" +
                    "  }," +
                    "  {" +
                    "    \"catId\": \"582dd4ee989b342ff665a123\"," +
                    "    \"isActive\": false," +
                    "    \"imgUrl\": \"https://static1.squarespace.com/static/57f708a637c581d5a1b8192b/t/581140f6cd0f68d0e2086ba5/1477525751663/holiday+lights.jpg?format=1500w\"," +
                    "    \"catTitle\": \"Atkinson\"" +
                    "  }," +
                    "  {" +
                    "    \"catId\": \"582dd4ee56e4c7a9187916b2\"," +
                    "    \"isActive\": true," +
                    "    \"imgUrl\": \"http://pinxmas.com/wp-content/uploads/2013/11/holidaychristmasxmaswallpapers-1385203254gkn48.jpg\"," +
                    "    \"catTitle\": \"Miles\"" +
                    "  }," +
                    "  {" +
                    "    \"catId\": \"582dd4ee729f3eaea8840d6a\"," +
                    "    \"isActive\": true," +
                    "    \"imgUrl\": \"https://static1.squarespace.com/static/57f708a637c581d5a1b8192b/t/581140f6cd0f68d0e2086ba5/1477525751663/holiday+lights.jpg?format=1500w\"," +
                    "    \"catTitle\": \"Harrell\"" +
                    "  }," +
                    "  {" +
                    "    \"catId\": \"582dd4eeb5899efcee2edbef\"," +
                    "    \"isActive\": true," +
                    "    \"imgUrl\": \"http://www.bumc.bu.edu/gms/files/2015/10/BME1.jpg\"," +
                    "    \"catTitle\": \"Doyle\"" +
                    "  }" +
                    "]";
        }
    }

    public static class ImageFeeder{

        public static ArrayList<Img> getImages(){

            ArrayList<Img> imgs = new ArrayList<>();
            for(int i=0; i<8; i++) {
                Img img = new Img();
                img.setImgId((i+1)+"");
                img.setImgPrice("€10");
                img.setImgUrl("http://imaginationcpl.com/developer/sportscity/images/"+(i+1)+".jpg");
                img.setIsFav("true");
                imgs.add(img);
            }
            return imgs;
        }

    }


}
