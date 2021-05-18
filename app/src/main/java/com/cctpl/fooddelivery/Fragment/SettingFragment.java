package com.cctpl.fooddelivery.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cctpl.fooddelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {

    Button mBtnEdit;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String UserId;

    CircleImageView mProfileImg;
    TextView mUserName,mMobileNumber;

    TextView mBtnAddress,mBtnOrderHistory,mPromoCode,mSetting,mFAQS,mSupport,mAbout,mLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        mBtnEdit = view.findViewById(R.id.btnEdit);
        mProfileImg = view.findViewById(R.id.profileImg);
        mUserName = view.findViewById(R.id.userName);
        mMobileNumber = view.findViewById(R.id.mobileNumber);

        mBtnAddress = view.findViewById(R.id.address);
        mBtnOrderHistory = view.findViewById(R.id.orderHistory);
        mPromoCode = view.findViewById(R.id.promoCode);
        mSetting = view.findViewById(R.id.setting);
        mFAQS = view.findViewById(R.id.faqs);
        mSupport = view.findViewById(R.id.support);
        mAbout = view.findViewById(R.id.about);
        mLogout = view.findViewById(R.id.logOut);

        //Call Address fragment
        mBtnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddressFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(null).commit();
            }
        });

        //Call Order Fragment
        mBtnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OrderHistoryFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(null).commit();
            }
        });

        //Call PromoCode Fragment
        mPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PromoCodeFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(null).commit();
            }
        });

        //Edit profile
        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditProfileFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(null).commit();
            }
        });

        firebaseFirestore.collection("Users").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String UserName = task.getResult().getString("UserName");
                            String MobileNumber = task.getResult().getString("MobileNumber");
                            String ProfileImg = task.getResult().getString("ProfileImgUrl");

                            mUserName.setText(UserName);
                            mMobileNumber.setText(MobileNumber);

                            if (!TextUtils.isEmpty(ProfileImg)){
                                Picasso.get().load(ProfileImg).into(mProfileImg);
                            }
                        }
                    }
                });
        return view;
    }
}