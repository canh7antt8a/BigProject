package com.t3h.bigproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.t3h.bigproject.R;
import com.t3h.bigproject.activity.MainActivity;
import com.t3h.bigproject.activity.UserMainActivity;
import com.t3h.bigproject.api.ApiBuilder;
import com.t3h.bigproject.model.User;
import com.t3h.bigproject.until.Dailoguntils;
import com.t3h.bigproject.until.ShareUntil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment implements View.OnClickListener, Callback<User> {

    private static final String KEY_LG_PASSWORD = "KEY_LOGIN_PASSWORD";
    private static final String KEY_LG_USERNAME = "KEY_LOGIN_USERNAME";
    private Button btnLogin;
    private Button btnRegisten;
    private TextInputLayout inputUserNameLg;
    private TextInputLayout inputPassWordLg;
    private Animation alpha;

    private ShareUntil shareUntil;
    private String userName;
    private String passWord;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_activity,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputUserNameLg = getActivity().findViewById(R.id.input_login_username);
        inputPassWordLg = getActivity().findViewById(R.id.input_login_password);
        btnLogin = getActivity().findViewById(R.id.btn_login);
        btnRegisten = getActivity().findViewById(R.id.btn_login_registen);
        alpha = AnimationUtils.loadAnimation(getContext(),R.anim.alpha_anmin);
        btnLogin.setOnClickListener(this);
        btnRegisten.setOnClickListener(this);


        shareUntil = new ShareUntil(getContext());
        inputUserNameLg.getEditText().setText(shareUntil.get(KEY_LG_USERNAME));
        inputPassWordLg.getEditText().setText(shareUntil.get(KEY_LG_PASSWORD));
        if (!inputUserNameLg.getEditText().getText().toString().isEmpty()&&
            !inputPassWordLg.getEditText().getText().toString().isEmpty()){

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:{
                Dailoguntils.showDialog(getContext());
                userName = inputUserNameLg.getEditText().getText().toString().trim();
                passWord = inputPassWordLg.getEditText().getText().toString().trim();
                //kiem tra tai khoan
                if (userName.isEmpty()||passWord.isEmpty()){
                    inputUserNameLg.setError("plase enter your username");
                    inputPassWordLg.setError("please enter your password");
                    Dailoguntils.cancelDialog();
                }else {
                   login();
                }
                break;
            }
            case R.id.btn_login_registen:{
                MainActivity act = (MainActivity) getActivity();
                act.showFragment(act.getRegistenFragment());
                break;
            }
        }
    }

    private void login(){
        ApiBuilder.getInstance().login(userName,passWord).enqueue(this);
    }
    public void finishRegistion(String userName,String password){
        inputUserNameLg.getEditText().setText(userName);
        inputPassWordLg.getEditText().setText(password);
    }
    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        Dailoguntils.cancelDialog();
        if (response.code() == 200){
            shareUntil.put(KEY_LG_PASSWORD,inputPassWordLg.getEditText().getText().toString().trim());
            shareUntil.put(KEY_LG_USERNAME,inputUserNameLg.getEditText().getText().toString().trim());
            User user = response.body();
            Intent intent = new Intent(getContext(),UserMainActivity.class);
            intent.putExtra(User.class.getName(),user);
            startActivity(intent);
            getActivity().finish();
            Dailoguntils.cancelDialog();
        }
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        Dailoguntils.cancelDialog();
        Toast.makeText(getContext(), "Connection to sever fail", Toast.LENGTH_SHORT).show();
    }
}
