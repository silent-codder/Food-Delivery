package com.cctpl.fooddelivery.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.fooddelivery.Adapter.ProductCatListAdapter;
import com.cctpl.fooddelivery.Adapter.ProductListAdapter;
import com.cctpl.fooddelivery.Model.ProductData;
import com.cctpl.fooddelivery.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView,recyclerView2,recyclerView3;
    List<ProductData> productData;
    ProductListAdapter productListAdapter;
    ProductCatListAdapter productCatListAdapter;
    FirebaseFirestore firebaseFirestore;
    SearchView searchView;
    TextView textView1;
    TextView textView2;
    RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView3 = view.findViewById(R.id.searchRecycleView);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        recyclerView2 = view.findViewById(R.id.recycleView2);
        searchView = view.findViewById(R.id.searchView);
        textView1 = view.findViewById(R.id.text);
        textView2 = view.findViewById(R.id.text2);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                LottieAnimationView lottieAnimationView = getView().findViewById(R.id.empty);
                TextView textView = getView().findViewById(R.id.emptyTxt);
                lottieAnimationView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.trim().length()<1)
                {
                    clear();
                    LottieAnimationView lottieAnimationView = getView().findViewById(R.id.empty);
                    TextView textView = getView().findViewById(R.id.emptyTxt);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                else {
//                    LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottie);
//                    TextView textView = view.findViewById(R.id.text);
//                    lottieAnimationView.setVisibility(View.GONE);
//                    textView.setVisibility(View.GONE);
                    SearchData(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().length()<1)
                {
                    clear();
                    LottieAnimationView lottieAnimationView = getView().findViewById(R.id.empty);
                    TextView textView = getView().findViewById(R.id.emptyTxt);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                else {

                    SearchData(newText);
                }
                return false;
            }
        });

        productData = new ArrayList<>();
        productListAdapter = new ProductListAdapter(productData);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(productListAdapter);

       allCategory();

        TextView AllCategory = view.findViewById(R.id.allCategory);
        TextView SnacksCategory = view.findViewById(R.id.snacks);
        TextView SweetsCategory = view.findViewById(R.id.sweet);
        TextView NamkinCategory = view.findViewById(R.id.namkin);
        TextView DrinksCategory = view.findViewById(R.id.drinks);
        TextView FastingCategory = view.findViewById(R.id.fasting);

        AllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrinksCategory.setBackgroundResource(R.drawable.cat_bg);
                DrinksCategory.setTextColor(getResources().getColor(R.color.secondary));

                //select category
                AllCategory.setBackgroundResource(R.drawable.all_cat_bg);
                AllCategory.setTextColor(getResources().getColor(R.color.white));

                SnacksCategory.setBackgroundResource(R.drawable.cat_bg);
                SnacksCategory.setTextColor(getResources().getColor(R.color.secondary));
                SweetsCategory.setBackgroundResource(R.drawable.cat_bg);
                SweetsCategory.setTextColor(getResources().getColor(R.color.secondary));
                NamkinCategory.setBackgroundResource(R.drawable.cat_bg);
                NamkinCategory.setTextColor(getResources().getColor(R.color.secondary));
                FastingCategory.setBackgroundResource(R.drawable.cat_bg);
                FastingCategory.setTextColor(getResources().getColor(R.color.secondary));

                recyclerView.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.GONE);

                LottieAnimationView lottieAnimationView = getView().findViewById(R.id.empty);
                TextView textView = getView().findViewById(R.id.emptyTxt);

                lottieAnimationView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                allCategory();
            }
        });

        SnacksCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCategory.setBackgroundResource(R.drawable.cat_bg);
                AllCategory.setTextColor(getResources().getColor(R.color.secondary));

                //select category
                SnacksCategory.setBackgroundResource(R.drawable.all_cat_bg);
                SnacksCategory.setTextColor(getResources().getColor(R.color.white));

                SweetsCategory.setBackgroundResource(R.drawable.cat_bg);
                SweetsCategory.setTextColor(getResources().getColor(R.color.secondary));
                NamkinCategory.setBackgroundResource(R.drawable.cat_bg);
                NamkinCategory.setTextColor(getResources().getColor(R.color.secondary));
                DrinksCategory.setBackgroundResource(R.drawable.cat_bg);
                DrinksCategory.setTextColor(getResources().getColor(R.color.secondary));
                FastingCategory.setBackgroundResource(R.drawable.cat_bg);
                FastingCategory.setTextColor(getResources().getColor(R.color.secondary));

                recyclerView.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);

                String Category = "Snacks";

                loadData(Category);
            }
        });

        SweetsCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCategory.setBackgroundResource(R.drawable.cat_bg);
                AllCategory.setTextColor(getResources().getColor(R.color.secondary));

                //select category
                SweetsCategory.setBackgroundResource(R.drawable.all_cat_bg);
                SweetsCategory.setTextColor(getResources().getColor(R.color.white));

                SnacksCategory.setBackgroundResource(R.drawable.cat_bg);
                SnacksCategory.setTextColor(getResources().getColor(R.color.secondary));
                NamkinCategory.setBackgroundResource(R.drawable.cat_bg);
                NamkinCategory.setTextColor(getResources().getColor(R.color.secondary));
                DrinksCategory.setBackgroundResource(R.drawable.cat_bg);
                DrinksCategory.setTextColor(getResources().getColor(R.color.secondary));
                FastingCategory.setBackgroundResource(R.drawable.cat_bg);
                FastingCategory.setTextColor(getResources().getColor(R.color.secondary));

                recyclerView.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);

                String Category = "Sweets";

                loadData(Category);
            }
        });

        NamkinCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCategory.setBackgroundResource(R.drawable.cat_bg);
                AllCategory.setTextColor(getResources().getColor(R.color.secondary));

                //select category
                NamkinCategory.setBackgroundResource(R.drawable.all_cat_bg);
                NamkinCategory.setTextColor(getResources().getColor(R.color.white));

                SnacksCategory.setBackgroundResource(R.drawable.cat_bg);
                SnacksCategory.setTextColor(getResources().getColor(R.color.secondary));
                SweetsCategory.setBackgroundResource(R.drawable.cat_bg);
                SweetsCategory.setTextColor(getResources().getColor(R.color.secondary));
                DrinksCategory.setBackgroundResource(R.drawable.cat_bg);
                DrinksCategory.setTextColor(getResources().getColor(R.color.secondary));
                FastingCategory.setBackgroundResource(R.drawable.cat_bg);
                FastingCategory.setTextColor(getResources().getColor(R.color.secondary));

                recyclerView.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);

                String Category = "Namkins";

                loadData(Category);
            }
        });

        DrinksCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCategory.setBackgroundResource(R.drawable.cat_bg);
                AllCategory.setTextColor(getResources().getColor(R.color.secondary));

                //select category
                DrinksCategory.setBackgroundResource(R.drawable.all_cat_bg);
                DrinksCategory.setTextColor(getResources().getColor(R.color.white));

                SnacksCategory.setBackgroundResource(R.drawable.cat_bg);
                SnacksCategory.setTextColor(getResources().getColor(R.color.secondary));
                SweetsCategory.setBackgroundResource(R.drawable.cat_bg);
                SweetsCategory.setTextColor(getResources().getColor(R.color.secondary));
                NamkinCategory.setBackgroundResource(R.drawable.cat_bg);
                NamkinCategory.setTextColor(getResources().getColor(R.color.secondary));
                FastingCategory.setBackgroundResource(R.drawable.cat_bg);
                FastingCategory.setTextColor(getResources().getColor(R.color.secondary));

                recyclerView.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);

                String Category = "Drinks";

                loadData(Category);
            }
        });

        FastingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCategory.setBackgroundResource(R.drawable.cat_bg);
                AllCategory.setTextColor(getResources().getColor(R.color.secondary));

                //select category
                FastingCategory.setBackgroundResource(R.drawable.all_cat_bg);
                FastingCategory.setTextColor(getResources().getColor(R.color.white));

                SnacksCategory.setBackgroundResource(R.drawable.cat_bg);
                SnacksCategory.setTextColor(getResources().getColor(R.color.secondary));
                SweetsCategory.setBackgroundResource(R.drawable.cat_bg);
                SweetsCategory.setTextColor(getResources().getColor(R.color.secondary));
                NamkinCategory.setBackgroundResource(R.drawable.cat_bg);
                NamkinCategory.setTextColor(getResources().getColor(R.color.secondary));
                DrinksCategory.setBackgroundResource(R.drawable.cat_bg);
                DrinksCategory.setTextColor(getResources().getColor(R.color.secondary));

                recyclerView.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);

                String Category = "Fasting";

                loadData(Category);
            }
        });

        //marquee text
        TextView marquee = view.findViewById(R.id.marquee);
        marquee.setSelected(true);
        return view;
    }

    private void SearchData(String string) {
        productData = new ArrayList<>();
        productCatListAdapter = new ProductCatListAdapter(productData);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView3.setAdapter(productCatListAdapter);
        firebaseFirestore.collection("Products").orderBy("ProductName").startAt(string).endAt(string+"\uf9ff" )
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (!value.isEmpty()){
//                    LottieAnimationView lottieAnimationView = getView().findViewById(R.id.empty);
//                    TextView textView = getView().findViewById(R.id.emptyTxt);
//
//                    lottieAnimationView.setVisibility(View.VISIBLE);
//                    textView.setVisibility(View.VISIBLE);
//                }

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String PostId = doc.getDocument().getId();
                        ProductData mData = doc.getDocument().toObject(ProductData.class).withId(PostId);
                        productData.add(mData);
                        productListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void clear() {
        int size = productData.size();
        productData.clear();
        productCatListAdapter.notifyItemRangeRemoved(0,size);
    }

    private void allCategory() {
        firebaseFirestore.collection("Products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int count = value.size();
                TextView countTxt = getView().findViewById(R.id.popularText);
                countTxt.setText("Popular Snacks (" + String.valueOf(count) + ")" );

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String PostId = doc.getDocument().getId();
                        ProductData mData = doc.getDocument().toObject(ProductData.class).withId(PostId);
                        productData.add(mData);
                        productListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void loadData(String category) {
        productData = new ArrayList<>();
        productCatListAdapter = new ProductCatListAdapter(productData);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setAdapter(productCatListAdapter);

        firebaseFirestore.collection("Products").whereEqualTo("Category",category)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int count = value.size();
                TextView countTxt = getView().findViewById(R.id.popularText);
                countTxt.setText("Popular Snacks (" + String.valueOf(count) + ")" );
                if (!value.isEmpty()){
                    LottieAnimationView lottieAnimationView = getView().findViewById(R.id.empty);
                    TextView textView = getView().findViewById(R.id.emptyTxt);

                    lottieAnimationView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String PostId = doc.getDocument().getId();
                        ProductData mData = doc.getDocument().toObject(ProductData.class).withId(PostId);
                        productData.add(mData);
                        productCatListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
//firebaseFirestore.collection("Users").orderBy("UserName").startAt(s).endAt(s+"\uf9ff" )
}