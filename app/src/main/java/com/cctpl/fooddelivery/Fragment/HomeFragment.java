package com.cctpl.fooddelivery.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    RecyclerView recyclerView,recyclerView2;

    List<ProductData> productData;
    ProductListAdapter productListAdapter;
    ProductCatListAdapter productCatListAdapter;
    FirebaseFirestore firebaseFirestore;
    SearchView searchView;
    TextView textView1;
    TextView textView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView2 = view.findViewById(R.id.recycleView2);
        searchView = view.findViewById(R.id.searchView);
        textView1 = view.findViewById(R.id.text);
        textView2 = view.findViewById(R.id.text2);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                return false;
            }
        });

        productData = new ArrayList<>();
        productListAdapter = new ProductListAdapter(productData);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(productListAdapter);

        firebaseFirestore.collection("Products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

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
                        productListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        TextView AllCategory = view.findViewById(R.id.allCategory);
        TextView SnacksCategory = view.findViewById(R.id.snacks);
        TextView SweetsCategory = view.findViewById(R.id.sweet);
        TextView NamkinCategory = view.findViewById(R.id.namkin);
        TextView DrinksCategory = view.findViewById(R.id.drinks);

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

                recyclerView.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.GONE);

                LottieAnimationView lottieAnimationView = getView().findViewById(R.id.empty);
                TextView textView = getView().findViewById(R.id.emptyTxt);

                lottieAnimationView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
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

                recyclerView.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);

                String Category = "Drinks";

                loadData(Category);
            }
        });
        return view;
    }

    private void loadData(String category) {
        productData = new ArrayList<>();
        productCatListAdapter = new ProductCatListAdapter(productData);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setAdapter(productCatListAdapter);

        firebaseFirestore.collection("Products").whereEqualTo("Category",category)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

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

}