package com.app.sportcity.fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sportcity.R;
import com.app.sportcity.adapters.CatAdapter;
import com.app.sportcity.objects.Category;
import com.app.sportcity.objects.CategorySer;
import com.app.sportcity.objects.Item;
import com.app.sportcity.server_protocols.ApiCalls;
import com.app.sportcity.server_protocols.RetrofitSingleton;
import com.app.sportcity.statics.StaticVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ArrayList<CategorySer> categories_temp;
    private ArrayList<Category> categories;
    String test;
    JSONArray jaTest;
    private ApiCalls apiCalls;

    public HomeFragment() {
        test = "[" +
                "  {" +
                "    \"catId\": \"582dd4eee6198f464c71d7b1\"," +
                "    \"isActive\": false," +
                "    \"imgUrl\": \"http://placehold.it/32x32\"," +
                "    \"catTitle\": \"Ramos\"" +
                "  }," +
                "  {" +
                "    \"catId\": \"582dd4eea26b86afd5e2cb20\"," +
                "    \"isActive\": false," +
                "    \"imgUrl\": \"http://placehold.it/32x32\"," +
                "    \"catTitle\": \"Teresa\"" +
                "  }," +
                "  {" +
                "    \"catId\": \"582dd4ee5f30dd5503178d49\"," +
                "    \"isActive\": false," +
                "    \"imgUrl\": \"http://placehold.it/32x32\"," +
                "    \"catTitle\": \"Sheena\"" +
                "  }," +
                "  {" +
                "    \"catId\": \"582dd4ee989b342ff665a123\"," +
                "    \"isActive\": false," +
                "    \"imgUrl\": \"http://placehold.it/32x32\"," +
                "    \"catTitle\": \"Atkinson\"" +
                "  }," +
                "  {" +
                "    \"catId\": \"582dd4ee56e4c7a9187916b2\"," +
                "    \"isActive\": true," +
                "    \"imgUrl\": \"http://placehold.it/32x32\"," +
                "    \"catTitle\": \"Miles\"" +
                "  }," +
                "  {" +
                "    \"catId\": \"582dd4ee729f3eaea8840d6a\"," +
                "    \"isActive\": true," +
                "    \"imgUrl\": \"http://placehold.it/32x32\"," +
                "    \"catTitle\": \"Harrell\"" +
                "  }," +
                "  {" +
                "    \"catId\": \"582dd4eeb5899efcee2edbef\"," +
                "    \"isActive\": true," +
                "    \"imgUrl\": \"http://placehold.it/32x32\"," +
                "    \"catTitle\": \"Doyle\"" +
                "  }" +
                "]";

        try {
            categories_temp = new ArrayList<>();
            jaTest = new JSONArray(test);
            for(int i = 0; i<=jaTest.length(); i++){
                JSONObject joTest = jaTest.getJSONObject(i);
                CategorySer categorySer = new CategorySer();
                categorySer.setCatId(joTest.getString("catId"));
                categorySer.setIsActive(joTest.getBoolean("isActive"));
                categorySer.setCatTitle(joTest.getString("catTitle"));
                categories_temp.add(categorySer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        apiCalls = RetrofitSingleton.getApiCalls();
//        getCategories(view);
        populateMenu(view);
        return view;
    }

    private void populateMenu(View view) {
        CatAdapter catAdapter = new CatAdapter(StaticVariables.ActiveMenuList.list, getContext());
        RecyclerView rvCats = (RecyclerView) view.findViewById(R.id.rv_cats);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvCats.setLayoutManager(mLayoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        rvCats.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvCats.setAdapter(catAdapter);
    }

//    private void getCategories(final View view) {
//        Call<List<Category>> categoryCall = apiCalls.getCategories();
//        categoryCall.enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//                System.out.println("Response size:" + response.body().size());
//                populateCategories(response.body(), view);
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }
//
//    private void populateCategories(List<Category> categories, View view) {
//        for (Category category : categories) {
//            System.out.println("Category Name:- " + category.getName()+" -- category id:- "+category.getId());
//        }
//        CatAdapter catAdapter = new CatAdapter(categories, getContext());
//        RecyclerView rvCats = (RecyclerView) view.findViewById(R.id.rv_cats);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
//        rvCats.setLayoutManager(mLayoutManager);
//
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
//        rvCats.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
//        rvCats.setAdapter(catAdapter);
//    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;

            // Add top margin only for the first item to avoid double space between items
//            if (parent.getChildLayoutPosition(view) == 0) {
//                outRect.top = space;
//            } else {
//                outRect.top = 0;
//            }
        }
    }

}
