package com.cctpl.fooddelivery.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.fooddelivery.Adapter.CardAdapter;
import com.cctpl.fooddelivery.Adapter.FinalCardAdapter;
import com.cctpl.fooddelivery.Model.CardData;
import com.cctpl.fooddelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class ConfirmOrderFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String UserId;

    TextView mBtnEditCard;
    TextView mTotalPrice;
    Button mBtnOrderNow;
    ProgressBar progressBar;
    int Total,ProductCount;

    RecyclerView recyclerView;
    List<CardData> cardData;
    FinalCardAdapter finalCardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.loader);
        mBtnOrderNow = view.findViewById(R.id.btnOrderNow);
        mTotalPrice = view.findViewById(R.id.totalPrice);
        mBtnEditCard = view.findViewById(R.id.btnEditCard);
        recyclerView = view.findViewById(R.id.recycleView);
        UserId = firebaseAuth.getCurrentUser().getUid();

        cardData = new ArrayList<>();
        finalCardAdapter = new FinalCardAdapter(cardData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(finalCardAdapter);

        firebaseFirestore.collection("Users")
                .document(UserId).collection("Card").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String CardId = doc.getDocument().getId();
                        CardData mData = doc.getDocument().toObject(CardData.class).withId(CardId);
                        cardData.add(mData);
                        finalCardAdapter.notifyDataSetChanged();
                    }
                }

            }
        });


        firebaseFirestore.collection("Users").document(UserId).collection("Card")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        Total = 00;
                        ProductCount = value.size();
                        for (DocumentChange doc : value.getDocumentChanges()){
                            Total = Total + Integer.valueOf(doc.getDocument().get("TotalPrice").toString());
                            Log.d(TAG, "onEvent: " + Total);
                            TextView subTotal = view.findViewById(R.id.subTotalPrice);
                            subTotal.setText("₹ " +Total);
                        }
                        Total += 50;
                        mTotalPrice.setText("₹ " +Total);
                    }
                });

        mBtnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new CheckOutFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(null).commit();

            }
        });

        mBtnEditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CartFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }
}