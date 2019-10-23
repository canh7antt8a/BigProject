package com.t3h.bigproject.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.t3h.bigproject.R;
import com.t3h.bigproject.api.ApiBuilder;
import com.t3h.bigproject.fragment.AccountFragment;
import com.t3h.bigproject.fragment.CartFragment;
import com.t3h.bigproject.fragment.DetailFragment;
import com.t3h.bigproject.fragment.FavoriteFragment;
import com.t3h.bigproject.fragment.HistoryFragment;
import com.t3h.bigproject.fragment.OrderFragment;
import com.t3h.bigproject.model.UploadAvatar;
import com.t3h.bigproject.model.User;
import com.t3h.bigproject.until.ShareUntil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, Callback<UploadAvatar> {
    private static final int REQUEST_PICK_IMAGE = 1;
    private static final String KEY_LG_PASSWORD = "KEY_LOGIN_PASSWORD";
    private static final String KEY_LG_USERNAME = "KEY_LOGIN_USERNAME";
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private ShareUntil shareUntil;
    private View header ;

    private final String[] PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Uri resultUri;


    private File file;
    private RequestBody fileName;
    private MultipartBody.Part fileToUpload;
    private CircleImageView avatar;
    private ImageButton imgbtnCameraPicker;
    private TextView tvUserName,tvStatus;

    private ImageView imgStatus;
    private Button btnConfirm;

    private User user;
    private OrderFragment orderFragment = new OrderFragment();
    private DetailFragment detailFragment = new DetailFragment();
    private CartFragment cartFragment = new CartFragment();
    private FavoriteFragment favoriteFragment = new FavoriteFragment();
    private HistoryFragment historyFragment = new HistoryFragment();
    private AccountFragment accountFragment = new AccountFragment();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView(savedInstanceState);
        initFragment();
        showFragment(orderFragment);
    }

    private void initView(Bundle savedInstanceState) {
        user = (User) getIntent().getSerializableExtra(User.class.getName());
        //naviogation bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);

        toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        avatar = header.findViewById(R.id.cimg_avatar);
        imgbtnCameraPicker = header.findViewById(R.id.imgbtn_camera_pick_image);
        if (user.getAvatar()!=null){
            Glide.with(avatar).load(ApiBuilder.BASE_URL+user.getAvatar()).into(avatar);
        }else{
            avatar.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }

        tvUserName = header.findViewById(R.id.tv_username);
        tvUserName.setText(user.getUserName());
        imgStatus = header.findViewById(R.id.img_status);
        imgStatus.setImageResource(R.drawable.online);
        tvStatus = header.findViewById(R.id.tv_user_status);
        tvStatus.setText("Online");
        btnConfirm = header.findViewById(R.id.btn_confirm);


        Menu menu = navigationView.getMenu();

        header.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        imgbtnCameraPicker.setOnClickListener(this);
    }

    private void initFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.ln_panel,orderFragment);
        transaction.add(R.id.ln_panel,detailFragment);
        transaction.add(R.id.ln_panel,cartFragment);
        transaction.add(R.id.ln_panel,favoriteFragment);
        transaction.add(R.id.ln_panel,historyFragment);
        transaction.add(R.id.ln_panel,accountFragment);
        transaction.commit();
    }

    public void showFragment(Fragment fr){
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(orderFragment);
        transaction.hide(detailFragment);
        transaction.hide(cartFragment);
        transaction.hide(favoriteFragment);
        transaction.hide(historyFragment);
        transaction.hide(accountFragment);
        transaction.show(fr);
        transaction.commit();
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkPermissions(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            for (String p:PERMISSION){
                if (checkSelfPermission(p)== PackageManager.PERMISSION_DENIED){
                    requestPermissions(PERMISSION,0);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.list_drink:{
                getOrderFragment().loadData();
                showFragment(orderFragment);
                break;
            }
            case R.id.nav_cart:{
                showFragment(cartFragment);
                break;
            }
            case R.id.nav_favorite_drink:{
                showFragment(favoriteFragment);
                favoriteFragment.loadData(user);
                break;
            }
            case R.id.nav_history:{
                showFragment(historyFragment);
                break;
            }
            case R.id.nav_option_update_account:{
                showFragment(accountFragment);
                break;
            }
            case R.id.nav_user_logout:{
                shareUntil = new ShareUntil(this);
                shareUntil.put(KEY_LG_USERNAME,"");
                shareUntil.put(KEY_LG_PASSWORD,"");
                Intent intent =  new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (orderFragment.isVisible()){
            super.onBackPressed();
        }else {
            getOrderFragment().loadData();
            showFragment(getOrderFragment());
        }

    }

    public OrderFragment getOrderFragment() {
        return orderFragment;
    }

    public DetailFragment getDetailFragment() {
        return detailFragment;
    }

    public CartFragment getCartFragment() {
        return cartFragment;
    }

    public HistoryFragment getHistoryFragment() {
        return historyFragment;
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgbtn_camera_pick_image:{
                    if (checkPermissions() == true) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(this);
                    }
                    break;
                }
                case R.id.btn_confirm:{
                    ApiBuilder.getInstance().uploadImage(fileToUpload,fileName).enqueue(this);
                    break;
                }
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){

                resultUri = result.getUri();
                avatar.setImageURI(resultUri);
                btnConfirm.setVisibility(View.VISIBLE);
                file = new File(resultUri.getPath());
                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("image",file.getName(),requestBody);
                fileName = RequestBody.create(MediaType.parse("text/plain"),file.getName());
            }
        }
    }

    @Override
    public void onResponse(Call<UploadAvatar> call, Response<UploadAvatar> response) {
        final UploadAvatar uploadAvatar = response.body();
        if (uploadAvatar.getStatus()==0){
            Toast.makeText(this, uploadAvatar.getMessage(), Toast.LENGTH_SHORT).show();
            btnConfirm.setVisibility(View.INVISIBLE);
        }else {
            ApiBuilder.getInstance().uploadAvatarDatabase(user.getUserName(),uploadAvatar.getImageName()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code()==200){
                        Toast.makeText(UserMainActivity.this, uploadAvatar.getMessage(), Toast.LENGTH_SHORT).show();
                        btnConfirm.setVisibility(View.INVISIBLE);
                    }else{
                        Toast.makeText(UserMainActivity.this, "upload fail", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(UserMainActivity.this, R.string.error404sever, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onFailure(Call<UploadAvatar> call, Throwable t) {
        Toast.makeText(this, R.string.error404sever, Toast.LENGTH_SHORT).show();
    }
}
