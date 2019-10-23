package com.t3h.bigproject.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.t3h.bigproject.R;
import com.t3h.bigproject.api.ApiBuilder;

import com.t3h.bigproject.model.UploadDrink;
import com.t3h.bigproject.model.User;
import com.t3h.bigproject.until.Dailoguntils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDrink extends AppCompatActivity implements View.OnClickListener, Callback<UploadDrink>, TextWatcher {
    private ImageView imvDrink;
    private EditText edtDrinkName;
    private EditText edtPrice;
    private RadioGroup rdgStatus;
    private EditText edtIntroduceDrink;
    private Button btnAddDrink;
    private RadioButton rdbtnStatus;
    private TextView tvErroImage;
    private Button btnCancel;

    private Uri resultUri;
    private File file;
    private RequestBody fileName;
    private MultipartBody.Part fileToUpload;
    private final String[] PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private User user;
    private Dailoguntils dailoguntils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
        initView();
    }

    private void initView() {
        user = (User) getIntent().getSerializableExtra(User.class.getName());
        dailoguntils = new Dailoguntils();
        imvDrink = findViewById(R.id.imv_drink);
        edtDrinkName = findViewById(R.id.edt_name_drink);
        rdgStatus = findViewById(R.id.rdg_status_drink);
        edtPrice = findViewById(R.id.edt_price_drink);
        edtIntroduceDrink = findViewById(R.id.edt_intro_drink);
        btnAddDrink = findViewById(R.id.btn_add_drink);
        tvErroImage = findViewById(R.id.tv_erro_not_add_image);
        btnCancel = findViewById(R.id.btn_cancel);

        edtDrinkName.addTextChangedListener(this);
        edtPrice.addTextChangedListener(this);
        edtIntroduceDrink.addTextChangedListener(this);

        btnAddDrink.setOnClickListener(this);
        imvDrink.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_drink:{
                dailoguntils.showDialog(this);
                int idRadioButton = rdgStatus.getCheckedRadioButtonId();
                rdbtnStatus = findViewById(idRadioButton);
                if (imvDrink.getDrawable()==null){
                    tvErroImage.setVisibility(View.VISIBLE);
                }else {
                    tvErroImage.setVisibility(View.INVISIBLE);
                }
                String drinkName = edtDrinkName.getText().toString().trim();
                String introduceDrink = edtIntroduceDrink.getText().toString().trim();
                String price = edtPrice.getText().toString().trim();

                if (drinkName.isEmpty()){
                    edtDrinkName.setError(getResources().getString(R.string.edt_emty));
                }
                if(introduceDrink.isEmpty()){
                    edtIntroduceDrink.setError(getResources().getString(R.string.edt_emty));
                }
                if (price.isEmpty()){
                    edtPrice.setError(getResources().getString(R.string.edt_emty));
                }
                if (!drinkName.isEmpty()&&!introduceDrink.isEmpty()&&!price.isEmpty()) {
                    ApiBuilder.getInstance().addImageDrink(fileToUpload, fileName).enqueue(this);
                }
                break;
            }
            case R.id.btn_cancel:{
                Intent intent = new Intent(this,UserMainActivity.class);
                intent.putExtra(User.class.getName(),user);
                startActivity(intent);
                break;
            }
            case R.id.imv_drink:{
                if (checkPermissions()){
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);}
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
                imvDrink.setImageURI(resultUri);
                file = new File(resultUri.getPath());
                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("image",file.getName(),requestBody);
                fileName = RequestBody.create(MediaType.parse("text/plain"),file.getName());
            }
        }
    }

    @Override
    public void onResponse(Call<UploadDrink> call, Response<UploadDrink> response) {
        dailoguntils.cancelDialog();
        final UploadDrink uploadDrink = response.body();

        if (uploadDrink.getStatus()==1){
            if (tvErroImage.getVisibility()==View.VISIBLE){
                tvErroImage.setVisibility(View.INVISIBLE);
            }
            //upload Image Name
            ApiBuilder.getInstance().insertImage(uploadDrink.getImage()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code()==200){
                    int statusNumber = 0;
                    String status =rdbtnStatus.getText().toString().trim();
                    if (status.equalsIgnoreCase(getResources().getString(R.string.status_still_sell))){
                        statusNumber = 1;
                    }else if (status.equalsIgnoreCase(getResources().getString(R.string.status_coming_soon)) ){
                        statusNumber = 2;
                    }else {
                        statusNumber = 3;
                    }
                    //upload infor drink
                        ApiBuilder.getInstance().addInforDrink(uploadDrink.getImage(),edtDrinkName.getText().toString().trim(),
                                edtPrice.getText().toString().trim(),
                                edtIntroduceDrink.getText().toString().trim(),
                                statusNumber).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                dailoguntils.cancelDialog();
                                if (response.code()==200){
                                    Toast.makeText(AddDrink.this, R.string.add_drink_success, Toast.LENGTH_SHORT).show();
                                    imvDrink.setImageResource(0);
                                    edtDrinkName.setText(null);
                                    edtIntroduceDrink.setText(null);
                                    edtPrice.setText(null);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                dailoguntils.cancelDialog();
                                Toast.makeText(AddDrink.this, R.string.add_drink_fail, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(AddDrink.this, "upload fail", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(AddDrink.this, "Lost Connect To sever", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            if (tvErroImage.getVisibility()==View.INVISIBLE){
                tvErroImage.setVisibility(View.VISIBLE);
                tvErroImage.setText(uploadDrink.getMessage());
            }else {
                tvErroImage.setText(uploadDrink.getMessage());
            }
        }
        dailoguntils.cancelDialog();
    }

    @Override
    public void onFailure(Call<UploadDrink> call, Throwable t) {
        dailoguntils.cancelDialog();
        if (tvErroImage.getVisibility()==View.INVISIBLE){
            tvErroImage.setVisibility(View.VISIBLE);
            tvErroImage.setText(R.string.add_drink_fail);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    @Override
    public void afterTextChanged(Editable editable) {
        String drinkName = edtDrinkName.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String introduceDrink = edtIntroduceDrink.getText().toString().trim();
        if (drinkName.length()>0){
            edtDrinkName.setError(null);
        }
        if (price.length()>0){
            edtPrice.setError(null);
        }
        if (introduceDrink.length()>0){
            edtIntroduceDrink.setError(null);
        }
    }

}
