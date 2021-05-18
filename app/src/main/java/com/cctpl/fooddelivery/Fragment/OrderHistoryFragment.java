package com.cctpl.fooddelivery.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.fooddelivery.Adapter.OrderAdapter;
import com.cctpl.fooddelivery.Model.CardData;
import com.cctpl.fooddelivery.Model.OrderData;
import com.cctpl.fooddelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryFragment extends Fragment {

    List<OrderData> orderData;
    OrderAdapter orderAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String UserId;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_order_history, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        orderData = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(orderAdapter);

        firebaseFirestore.collection("Orders").whereEqualTo("UserId",UserId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.isEmpty()){
                            LottieAnimationView lottieAnimationView = view.findViewById(R.id.empty);
                            TextView textView = view.findViewById(R.id.emptyTxt);

                            lottieAnimationView.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);

                        }else {

                            LottieAnimationView lottieAnimationView = view.findViewById(R.id.empty);
                            TextView textView = view.findViewById(R.id.emptyTxt);

                            lottieAnimationView.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                        }

                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String OrderId = doc.getDocument().getId();
                                OrderData mData = doc.getDocument().toObject(OrderData.class).withId(OrderId);
                                orderData.add(mData);
                                orderAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        return view;
    }
}