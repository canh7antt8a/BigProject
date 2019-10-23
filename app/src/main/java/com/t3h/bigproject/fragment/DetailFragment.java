package com.t3h.bigproject.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.t3h.bigproject.R;
import com.t3h.bigproject.activity.UserMainActivity;
import com.t3h.bigproject.adapter.CommentAdapter;
import com.t3h.bigproject.api.ApiBuilder;
import com.t3h.bigproject.model.Comment;
import com.t3h.bigproject.model.Order;
import com.t3h.bigproject.model.User;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {
    private ImageView imvDrink;
    private TextView tvNameDrink;
    private RatingBar rtAllRate;
    private RatingBar rtYourRate;
    private CommentAdapter adapter;
    private ArrayList<Comment> data;
    private TextView tvAllRateNumber;
    private TextView tvYourRateNumber;
    private TextView tvContentIntroduceDrink;
    private RecyclerView rcvDetail;
    private int id;
    private float rate = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_detail,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public void loadData() {
        rtYourRate.setRating(0);
        tvYourRateNumber.setText("0");
        ApiBuilder.getInstance().getComment(id).enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                if (response.code()==200){
                    rate =0;
                    data = response.body();
                    adapter.setData(data);
                    rcvDetail.setAdapter(adapter);
                    setYourRating();
                    setNumberCommentDrink();
                    for (int i = 0; i < data.size(); i++) {
                        rate+=data.get(i).getRate();
                    }
                    if (rtYourRate.getRating()!=0){
                        rtYourRate.setIsIndicator(true);
                    }else {
                        rtYourRate.setIsIndicator(false);
                        rtYourRate.setRating(0);
                    }
                    if (rate!=0){
                        rate = (float) (Math.floor(rate/data.size() * 10) / 10);
                        rtAllRate.setRating(rate);
                    }
                    tvAllRateNumber.setText(String.valueOf(rate));
                    setAllRatingDatabase();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {

            }
        });


    }

    private void initView() {
        imvDrink = getActivity().findViewById(R.id.imv_detail);
        tvNameDrink = getActivity().findViewById(R.id.tv_name_drink_detail);
        rtAllRate = getActivity().findViewById(R.id.rb_all_rating);
        rtYourRate = getActivity().findViewById(R.id.rb_your_rating);
        tvAllRateNumber = getActivity().findViewById(R.id.tv_all_rate_number_detail);
        tvYourRateNumber = getActivity().findViewById(R.id.tv_your_number_rate_detail);
        tvContentIntroduceDrink = getActivity().findViewById(R.id.tv_introduce_drink);
        rcvDetail = getActivity().findViewById(R.id.rcv_comment);
        adapter = new CommentAdapter(getContext());

        rtYourRate.setOnRatingBarChangeListener(this);
    }

    private User takeUser(){
        UserMainActivity userMainActivity = (UserMainActivity) getActivity();
        User user = userMainActivity.getUser();
        return user;
    }
    private void setYourRating(){
        for (int  i= 0; i < data.size(); i++) {
            if (data.get(i).getUserName().equals(takeUser().getUserName())){
                rtYourRate.setRating(data.get(i).getRate());
                tvYourRateNumber.setText(String.valueOf(data.get(i).getRate()));
            }
        }
    }
    private void setNumberCommentDrink(){
        ApiBuilder.getInstance().addNumberCommentDrink(id,data.size()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void setAllRatingDatabase(){
        ApiBuilder.getInstance().addRateToDrink(id,rate).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public void takeId(Order order){
        id = order.getId();
        Glide.with(imvDrink)
                .load(ApiBuilder.BASE_URL+order.getImageLink())
                .into(imvDrink);
        tvNameDrink.setText(order.getNameDrink());
        rtAllRate.setRating(order.getRate());
        float rate = order.getRate();
        tvAllRateNumber.setText(String.valueOf(order.getRate()));
        tvContentIntroduceDrink.setText(order.getIntroduceDrink());
        loadData();
    }


    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            if (b == true){
                AlertDialog.Builder dialogAlert = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.dialog_comment,null);
                dialogAlert.setView(view);
                final float ratingStar = v;
                final EditText edtComment = view.findViewById(R.id.edt_comment);
                dialogAlert.setPositiveButton("Comment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserMainActivity userMainActivity = (UserMainActivity) getActivity();
                        User user = userMainActivity.getUser();
                        ApiBuilder.getInstance().writeComment(id,user.getUserName(),edtComment.getText().toString().trim(),ratingStar).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code()==200){
                                    loadData();
                                }else {
                                    Toast.makeText(getContext(), "commnet fail", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                });
                AlertDialog dialog = dialogAlert.create();
                dialog.show();
            }else {

            }
    }

}
