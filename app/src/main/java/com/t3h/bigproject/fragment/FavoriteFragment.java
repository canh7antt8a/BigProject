package com.t3h.bigproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.bigproject.R;
import com.t3h.bigproject.activity.UserMainActivity;
import com.t3h.bigproject.adapter.OrderAdapter;
import com.t3h.bigproject.api.ApiBuilder;
import com.t3h.bigproject.model.Cart;
import com.t3h.bigproject.model.Order;
import com.t3h.bigproject.model.User;
import com.t3h.bigproject.until.Dailoguntils;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment implements OrderAdapter.itemListenner {
    private RecyclerView rcvFavorite;
    private OrderAdapter adapter;
    private ArrayList<Order> data;
    private Dailoguntils dailoguntils;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initVeiw();
    }

    private void initVeiw() {
        dailoguntils.showDialog(getContext());
        adapter = new OrderAdapter(getContext());
        adapter.setListenner(this);
        rcvFavorite = getActivity().findViewById(R.id.rcv_favorite);
        loadData(takeUser());
    }
    private User takeUser(){
        UserMainActivity userMainActivity = (UserMainActivity) getActivity();
        User user = userMainActivity.getUser();
        return user;
    }
    public void loadData(User user){
        ApiBuilder.getInstance().getFavorite(user.getId()).enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                if (response.code() == 200) {
                    data =response.body();
                    adapter.setData(data);
                    rcvFavorite.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(), "Erro get Cart", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {

            }
        });
    }

    private void addCart(final int position){
        dailoguntils.showDialog(getContext());
        if(data.get(position).getStatus()==1) {
            ApiBuilder.getInstance().checkCart(takeUser().getId(), data.get(position).getId()).enqueue(new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    Cart cart = response.body();
                    if (cart != null) {
                        Toast.makeText(getContext(), "drink have been added", Toast.LENGTH_LONG).show();
                    } else {
                        ApiBuilder.getInstance().addCart(takeUser().getId(), data.get(position).getId(),1).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                dailoguntils.cancelDialog();
                                if (response.code() == 200) {
                                    Toast.makeText(getContext(), "add success", Toast.LENGTH_SHORT).show();
                                    UserMainActivity userMA = (UserMainActivity) getActivity();
                                    userMA.getCartFragment().loadData(takeUser());
                                } else {
                                    Toast.makeText(getContext(), "unable to add", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                dailoguntils.cancelDialog();
                                Toast.makeText(getContext(), "error 404 can't connect to sever", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    dailoguntils.cancelDialog();
                }
            });
        }else {
            dailoguntils.cancelDialog();
            Toast.makeText(getContext(), "this once isn't selling yet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void itemOnclickListener(int position) {
        UserMainActivity userMA = (UserMainActivity) getActivity();
        userMA.getDetailFragment().takeId(data.get(position));
        userMA.showFragment(userMA.getDetailFragment());
    }

    @Override
    public void imvMenuOnClickListenner(final int position, View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(),v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.delete_drink:{
                        ApiBuilder.getInstance().deleteFavorite(takeUser().getId(),data.get(menuItem.getOrder()).getId()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code()==200){
                                    Toast.makeText(getContext(), "delete success", Toast.LENGTH_SHORT).show();
                                    loadData(takeUser());
                                }else {
                                    Toast.makeText(getContext(), "delete fail", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                        break;
                    }
                    case R.id.add_cart_drink:{
                        addCart(position);
                        break;
                    }
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.menu_option_drink);
        UserMainActivity userMainActivity = (UserMainActivity) getActivity();
        User user = userMainActivity.getUser();
        if (user.getPermission().equalsIgnoreCase("false")){
            popupMenu.getMenu().findItem(R.id.add_favorist_menu_option).setVisible(false);
        }
        popupMenu.show();
    }

    @Override
    public void imvCartOnClickListennet(int position) {
        addCart(position);
    }

}
