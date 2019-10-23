package com.t3h.bigproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.t3h.bigproject.R;
import com.t3h.bigproject.fragment.LoginFragment;
import com.t3h.bigproject.fragment.RegistenFragment;

public class MainActivity extends AppCompatActivity {
    private FrameLayout panel;
    private LoginFragment loginFragment = new LoginFragment();
    private RegistenFragment registenFragment = new RegistenFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTransition();
        showFragment(loginFragment);
    }

    private void initView() {
        panel = findViewById(R.id.panel);
    }

    public void initTransition(){
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        transition.add(R.id.panel,loginFragment);
        transition.add(R.id.panel,registenFragment);
        transition.commit();
    }


    public void showFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(loginFragment);
        transaction.hide(registenFragment);
        transaction.show(fragment);
        transaction.commit();
    }

    public LoginFragment getLoginFragment() {
        return loginFragment;
    }

    public RegistenFragment getRegistenFragment() {
        return registenFragment;
    }

    @Override
    public void onBackPressed() {
        if (!registenFragment.isVisible()) {
            super.onBackPressed();
        }else{
            showFragment(getLoginFragment());
        }
    }
}
