package com.cctpl.fooddelivery.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.fooddelivery.Fragment.ProductViewFragment;
import com.cctpl.fooddelivery.Model.ProductData;
import com.cctpl.fooddelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductCatListAdapter extends RecyclerView.Adapter<ProductCatListAdapter.ViewHolder>{
    List<ProductData> productData;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Context context;
    String UserId;

    public ProductCatListAdapter(List<ProductData> productData) {
        this.productData = productData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cat_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ProductName = productData.get(position).getProductName();
        String Price = productData.get(position).getPrice();
        String Weight = productData.get(position).getUnit();
        String ProductImg = productData.get(position).getProductImgUrl();
        String ProductId = productData.get(position).ProductId;

        Picasso.get().load(ProductImg).into(holder.mProductImg);
        holder.mProductName.setText(ProductName);
        holder.mWeight.setText(Weight);
        holder.mPrice.setText("₹ "+Price);

        holder.mUnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mLike.setVisibility(View.VISIBLE);
                holder.mUnLike.setVisibility(View.GONE);

                HashMap<String,Object> map = new HashMap<>();
                map.put("TimeStamp",System.currentTimeMillis());
                firebaseFirestore.collection("Products").document(ProductId).collection("Likes").document(UserId).set(map);
            }
        });

        holder.mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mUnLike.setVisibility(View.VISIBLE);
                holder.mLike.setVisibility(View.GONE);
                firebaseFirestore.collection("Products").document(ProductId).collection("Likes").document(UserId).delete();
            }
        });

        firebaseFirestore.collection("Products").document(ProductId).collection("Likes").document(UserId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()){
                            holder.mLike.setVisibility(View.VISIBLE);
                        }
                    }
                });

        holder.mProductImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new ProductViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ProductId",ProductId);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(null).commit();
            }
        });
        holder.mBtnAddToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new ProductViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ProductId",ProductId);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mProductName;
        TextView mPrice;
        TextView mWeight;
        CircleImageView mProductImg;
        ImageView mUnLike;
        ImageView mLike;
        Button mBtnAddToCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPrice = itemView.findViewById(R.id.productPrice);
            mProductImg = itemView.findViewById(R.id.productImg);
            mWeight = itemView.findViewById(R.id.productWeight);
            mProductName = itemView.findViewById(R.id.productName);
            mUnLike = itemView.findViewById(R.id.unlike);
            mLike = itemView.findViewById(R.id.like);
            mBtnAddToCard = itemView.findViewById(R.id.btnAddCard);
        }
    }
}
