package com.t3h.bigproject.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.t3h.bigproject.R;
import com.t3h.bigproject.activity.MainActivity;
import com.t3h.bigproject.api.ApiBuilder;
import com.t3h.bigproject.until.Dailoguntils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistenFragment extends Fragment implements View.OnClickListener, TextWatcher, Callback<ResponseBody> {

    private static Pattern PASSWORD_PATTERN=
            Pattern.compile("^" +
                            "(?=.*[0-9])" +         //at last 1 number
                            "(?=.*[a-z])"+          //at last 1 lower case
                            "(?=.*[A-Z])" +         //at last 1 uper case
                            "(?=\\S+$)" +           //no white space
                            ".{6,}$"                //at least 6 charater
                    );
    private Button btnCreate;

    private  Calendar curentDate = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    private TextInputLayout
            inputUserName,inputPassword
            ,inputFullName,inputEmail,
            inputPhoneNumber,inputDateOfBirth;

    private TextInputEditText
            textInputEditUserName,textInputEditPassword,
            textInputEditFullName,textInputEditIndentity
            ,textInputEditEmail,textInputEditPhoneNumber,
            textInputEditDatOfBirth;

    private ArrayList checkingUserName = new ArrayList();
    private ArrayList checkEmail = new ArrayList();
    private Button btnCheckUserName,btnCheckEmail;
    private Dailoguntils dailoguntils;
    private RadioGroup radioSexGroup;
    private RadioButton radioButtonSex;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dailoguntils = new Dailoguntils();
        inputUserName    = addIdTextInputLayout(R.id.input_username);
        inputPassword    = addIdTextInputLayout(R.id.input_password);
        inputFullName    = addIdTextInputLayout(R.id.input_fullname);
        inputDateOfBirth = addIdTextInputLayout(R.id.input_dateofbirth);
        inputEmail       = addIdTextInputLayout(R.id.input_email);
        inputPhoneNumber = addIdTextInputLayout(R.id.input_phonenumber);

        textInputEditUserName    = addIdTextInputEditText(R.id.txtinputedt_username);
        textInputEditPassword    = addIdTextInputEditText(R.id.txtinputedt_password);
        textInputEditFullName    = addIdTextInputEditText(R.id.txtinputedt_fullname);
        textInputEditEmail       = addIdTextInputEditText(R.id.txtinputedt_email);
        textInputEditPhoneNumber = addIdTextInputEditText(R.id.txtinputedt_phonenumber);
        textInputEditDatOfBirth  = addIdTextInputEditText(R.id.txtinputedt_dateofbirth);

        btnCheckEmail = getActivity().findViewById(R.id.btn_check_user_name);
        btnCheckUserName = getActivity().findViewById(R.id.btn_check_user_email);

        radioSexGroup = getActivity().findViewById(R.id.rdg_sex_group);
        btnCreate = getActivity().findViewById(R.id.btn_registen_login);
        btnCheckEmail.setOnClickListener(this);
        btnCheckUserName.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        btnCreate.setClickable(false);
    }

    private TextInputEditText addIdTextInputEditText(@IdRes int id){
        TextInputEditText inputEditText   = getActivity().findViewById(id);
        inputEditText.addTextChangedListener(this);
        if (id == R.id.txtinputedt_dateofbirth){
            inputEditText.setOnClickListener(this);
        }
        return inputEditText;
    }

    private TextInputLayout addIdTextInputLayout(@IdRes int idTextinputLayout){
        TextInputLayout textInputLayout = getActivity().findViewById(idTextinputLayout);
             return textInputLayout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_check_user_name:{

                final String strUserName = inputUserName.getEditText().getText().toString().trim();

                ApiBuilder.getInstance().checkUser(strUserName).enqueue(new Callback<ArrayList>() {
                    @Override
                    public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {

                        if (response.code()==401){
                            inputUserName.setError(null);
                        }else{
                            checkingUserName.add(response.body());
                            if (checkingUserName.size()>0){
                                inputUserName.setError("this user name already exits");
                            }
                        }

                    }
                    @Override
                    public void onFailure(Call<ArrayList> call, Throwable t) {

                    }
                });
                break;
            }

            case R.id.btn_check_user_email:{

                final String strEmail =   inputEmail.getEditText().getText().toString().trim();

                ApiBuilder.getInstance().checkEmail(strEmail).enqueue(new Callback<ArrayList>() {
                    @Override
                    public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {

                        if (response.code()==401){
                            inputEmail.setError(null);
                        } else{
                            checkEmail.add(response.body());
                            if (checkEmail.size()>0){
                                inputEmail.setError("this email have been used");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ArrayList> call, Throwable t) {

                    }
                });
                break;
            }
            case R.id.btn_registen_login:{
                checkEmail.clear();
                checkingUserName.clear();
                dailoguntils.showDialog(getContext());
                //validate for form
                final String strUserName = inputUserName.getEditText().getText().toString().trim();
                String strPassword = inputPassword.getEditText().getText().toString().trim();
                String strFullName = inputFullName.getEditText().getText().toString().trim();
                final String strEmail =   inputEmail.getEditText().getText().toString().trim();
                String strPhoneNumber = inputPhoneNumber.getEditText().getText().toString().trim();
                String strDateOfBirth = inputDateOfBirth.getEditText().getText().toString();
                int idRadioButton = radioSexGroup.getCheckedRadioButtonId();
                radioButtonSex = getActivity().findViewById(idRadioButton);
                String sex =radioButtonSex.getText().toString();
                int yearOld = 0;
                //password
                if (!PASSWORD_PATTERN.matcher(strPassword).matches()){
                    inputPassword.setError("your password is to weak");
                }else {
                    inputPassword.setError(null);
                }
                //dateofbirth
                if (!strDateOfBirth.isEmpty()){
                    Calendar calendar = Calendar.getInstance();
                    curentDate.getTime();
                    try {
                        calendar.setTime(format.parse(inputDateOfBirth.getEditText().getText().toString()));
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        int day = calendar.get(Calendar.DATE);
                        //yearold
                        if (curentDate.get(Calendar.YEAR)-year>=12&&
                                curentDate.get(Calendar.MONTH)>month){
                            yearOld = curentDate.get(Calendar.YEAR)-year;
                        }else if (curentDate.get(Calendar.YEAR)-year>=12&&
                                curentDate.get(Calendar.MONTH)==month&&
                                curentDate.get(Calendar.DATE)==day)
                        {
                            yearOld = curentDate.get(Calendar.YEAR)-year;
                        }else{
                            yearOld = curentDate.get(Calendar.YEAR)-year-1;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                ApiBuilder.getInstance().checkUser(strUserName).enqueue(new Callback<ArrayList>() {
                    @Override
                    public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
                        if (response.body()!=null){
                            checkingUserName.add(response.body());
                            inputUserName.setError("this user name already exits");
                        }
                    }
                    @Override
                    public void onFailure(Call<ArrayList> call, Throwable t) {

                    }
                });

                ApiBuilder.getInstance().checkEmail(strEmail).enqueue(new Callback<ArrayList>() {
                    @Override
                    public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
                        if (response.body()!=null){
                            checkEmail.add(response.body());
                            inputEmail.setError("this email have been used");
                        }
                    }
                    @Override
                    public void onFailure(Call<ArrayList> call, Throwable t) {

                    }
                });

                if (yearOld>=5
                        &&strPhoneNumber.length() >= 9&&
                        checkingUserName.size()==0&&
                        checkEmail.size()==0){
                    dailoguntils.showDialog(getContext());
                    ApiBuilder.getInstance().
                            register(strUserName,null,strPassword,strFullName,strDateOfBirth,yearOld,strPhoneNumber,
                            strEmail,"false",sex).enqueue(this);
                }else if (yearOld<5){
                    dailoguntils.cancelDialog();
                    inputDateOfBirth.setError("you to young");
                }else if (strPhoneNumber.length()>11||strPhoneNumber.length()<9){
                    dailoguntils.cancelDialog();
                    inputPhoneNumber.setError("this phonenumber not exist");
                }else{
                    dailoguntils.cancelDialog();
                }
                break;
            }
            case R.id.txtinputedt_dateofbirth:{
                final Calendar dateOfBirth = Calendar.getInstance();
                curentDate.getTime();
                int day = dateOfBirth.get(Calendar.DATE);
                int month = dateOfBirth.get(Calendar.MONTH);
                int year = dateOfBirth.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),android.R.style.Theme_Material_Light_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //i:year i1:month i2:day
                        dateOfBirth.set(i,i1,i2);
                        textInputEditDatOfBirth.setText(format.format(dateOfBirth.getTime()));
                    }
                },year,month,day);
                datePickerDialog.show();

                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //validate for form
        String strUserName = inputUserName.getEditText().getText().toString().trim();
        String strPassword = inputPassword.getEditText().getText().toString().trim();
        String strFullName = inputFullName.getEditText().getText().toString().trim();
        String strEmail = inputEmail.getEditText().getText().toString().trim();
        String strPhoneNumber = inputPhoneNumber.getEditText().getText().toString().trim();
        String strDateOfBirth = inputDateOfBirth.getEditText().getText().toString();
        //userName
        if (strUserName.length()<20&&!strUserName.isEmpty()){
            inputUserName.setError(null);
        }else if (strUserName.length() >50){
            inputUserName.setError("please enter user name less than 20 word");
        }
        //password
         if (!PASSWORD_PATTERN.matcher(strPassword).matches()&&!strPassword.isEmpty()){
            inputPassword.setError("Your password weak");
        }else{
            inputPassword.setError(null);
        }
        //fullname
        if (strFullName.length()<50&&!strFullName.isEmpty()){
            inputFullName.setError(null);
        }else if (strFullName.length()>50){
            inputFullName.setError("this name dosen't exist");
        }
        //Email
        if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()&&!strEmail.isEmpty()){
            inputEmail.setError("Email not exits");
        }else {
            inputEmail.setError(null);
        }
        //phonenumber
        if (strPhoneNumber.length()<=11&&!strPhoneNumber.isEmpty()){
            inputPhoneNumber.setError(null);
        }else if (strPhoneNumber.length() >11){
            inputPhoneNumber.setError("this phone number not exist");
        }
        //date of bith
        if (!strDateOfBirth.isEmpty()){
            Calendar checking = Calendar.getInstance();
            try {
                checking.setTime(format.parse(inputDateOfBirth.getEditText().getText().toString()));
                if (checking.get(Calendar.YEAR) == curentDate.get(Calendar.YEAR)){
                    inputDateOfBirth.setError("please check you year");
                }else {
                    inputDateOfBirth.setError(null);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //permission to click
        if (!strUserName.isEmpty()&&
            !strPassword.isEmpty()&&
            !strFullName.isEmpty()&&
            !strDateOfBirth.isEmpty()&&
            !strEmail.isEmpty()&&
            !strPhoneNumber.isEmpty()&&
                Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()){
            btnCreate = getActivity().findViewById(R.id.btn_registen_login);
            btnCreate.setClickable(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.code() == 200){
            MainActivity act = (MainActivity) getActivity();
            act.getLoginFragment().finishRegistion(inputUserName.getEditText().getText().toString().trim(),inputPassword.getEditText().getText().toString().trim());
            inputUserName.getEditText().setText("");
            inputPassword.getEditText().setText("");
            inputFullName.getEditText().setText("");
            inputEmail.getEditText().setText("");
            inputPhoneNumber.getEditText().setText(null);
            inputDateOfBirth.getEditText().setText("");
            act.showFragment(act.getLoginFragment());
            dailoguntils.cancelDialog();
        }else {
            dailoguntils.cancelDialog();
            Toast.makeText(getContext(), "Register fail", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        dailoguntils.cancelDialog();
        Toast.makeText(getContext(), "register fail", Toast.LENGTH_SHORT).show();
    }
}

