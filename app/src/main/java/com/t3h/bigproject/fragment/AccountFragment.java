package com.t3h.bigproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.t3h.bigproject.R;


import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private CircleImageView avatarAccount;
    private ImageButton imgbtnChoseAvatar;
    private Button btnConfirmAccount;
    private TextView tvErroImage;
    private TextView tvFullName;
    private TextView tvPassword;
    private TextView tvDateOfBirh;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private TextView tvSex;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        avatarAccount = getActivity().findViewById(R.id.cimg_avatar_account);
        imgbtnChoseAvatar = getActivity().findViewById(R.id.imgbtn_camera_pick_image_account);
        btnConfirmAccount = getActivity().findViewById(R.id.btn_confirm_account);
        tvErroImage = setIdForView(R.id.tv_erro_not_add_image_acount);
        tvFullName = setIdForView(R.id.tv_full_name_change_account);
        tvPassword = setIdForView(R.id.tv_password_change_account);
        tvDateOfBirh = setIdForView(R.id.tv_date_of_birth_change_account);
        tvEmail = setIdForView(R.id.tv_email_change_account);
        tvPhoneNumber = setIdForView(R.id.tv_phone_number_change_account);
        tvSex = setIdForView(R.id.tv_sex_change_account);

    }

    private TextView setIdForView(@IdRes int id){
        getActivity().findViewById(id).setOnClickListener(this);
        return getActivity().findViewById(id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_full_name_change_account:{
                Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
