package com.t3h.bigproject.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.t3h.bigproject.R;
import com.t3h.bigproject.activity.UserMainActivity;
import com.t3h.bigproject.adapter.CartAdapter;
import com.t3h.bigproject.api.ApiBuilder;
import com.t3h.bigproject.model.Cart;
import com.t3h.bigproject.model.Order;
import com.t3h.bigproject.model.User;
import com.t3h.bigproject.until.Dailoguntils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment implements CartAdapter.itemOnclick, View.OnClickListener {
    private RecyclerView rcvCart;
    private CartAdapter adapter;
    private ArrayList<Order> data;
    private TextView tvPrice;
    private Button btnSendToOrderTable;
    private int allPrice;
    private Dailoguntils dailoguntils;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        adapter = new CartAdapter(getContext());
        rcvCart = getActivity().findViewById(R.id.rcv_cart);

        btnSendToOrderTable = getActivity().findViewById(R.id.btn_add_order);
        btnSendToOrderTable.setOnClickListener(this);
        tvPrice = getActivity().findViewById(R.id.tv_price_cart);
        adapter.setListenner(this);
        loadData(takeUser());
    }

    private User takeUser(){
        UserMainActivity userMainActivity = (UserMainActivity) getActivity();
        User user = userMainActivity.getUser();
        return user;
    }

    public void loadData(User user){
        dailoguntils.showDialog(getContext());
        ApiBuilder.getInstance().getAllCartItem(user.getId()).enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                dailoguntils.cancelDialog();
                if (response.code() == 200) {
                    data =response.body();
                    adapter.setData(data);
                    rcvCart.setAdapter(adapter);
                    tvPrice.setText(getAllPrice(0)+"VND");
                }else {
                    Toast.makeText(getContext(), "Erro get Cart", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                dailoguntils.cancelDialog();
            }
        });
    }

    private int getAllPrice(int numberDrink){
        allPrice = 0;
        if (numberDrink==0){
            for (int i = 0; i < data.size(); i++) {
                String check = data.get(i).getPrice().replace("VND","");
                allPrice += Integer.parseInt(check)*data.get(i).getNumberOrder();
            }
        }else {
            for (int i = 0; i < data.size(); i++) {
                String check = data.get(i).getPrice().replace("VND","");
                allPrice += Integer.parseInt(check)*numberDrink;
            }
        }
        return allPrice;
    }
    @Override
    public void imgvPlusOnclick(int position) {
        int numberDrink = data.get(position).getNumberOrder();
        numberDrink++;
        final int finalNumberDrink = numberDrink;
        ApiBuilder.getInstance().updateNumberDrink(takeUser().getId(),data.get(position).getId(),numberDrink).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getAllPrice(finalNumberDrink);
                loadData(takeUser());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void imgvMinusOnclick(int position) {

        int numberDrink = data.get(position).getNumberOrder();
        if (numberDrink>1){
            numberDrink--;
            final int finalNumberDrink = numberDrink;
            ApiBuilder.getInstance().updateNumberDrink(takeUser().getId(),data.get(position).getId(),numberDrink).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    getAllPrice(finalNumberDrink);
                    loadData(takeUser());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

    }

    @Override
    public void itemOnclickListener(int position) {
        UserMainActivity userMA = (UserMainActivity) getActivity();
        userMA.getDetailFragment().takeId(data.get(position));
        userMA.showFragment(userMA.getDetailFragment());
    }

    @Override
    public void itemMenuOnclickListener(final int postion, View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(),v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.delete_drink:{
                        ApiBuilder.getInstance().deleteCart(takeUser().getId(),data.get(menuItem.getOrder()).getId()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(getContext(), "delete drink success", Toast.LENGTH_SHORT).show();
                                loadData(takeUser());
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), "delete drink fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    }
                    case R.id.add_favorist_menu_option:{
                        ApiBuilder.getInstance().checkFavorite(takeUser().getId(),data.get(postion).getId()).enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                Cart cart = response.body();
                                if (cart != null){
                                    Toast.makeText(getContext(), "this once already exist", Toast.LENGTH_SHORT).show();
                                }else {
                                    ApiBuilder.getInstance().addFavorite(takeUser().getId(),data.get(postion).getId()).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            Toast.makeText(getContext(), "add success", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(getContext(), "add fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {

                            }
                        });

                        break;
                    }
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.menu_option_drink);
        popupMenu.getMenu().findItem(R.id.add_cart_drink).setVisible(false);
        popupMenu.show();
    }

    @Override
    public void onClick(View view) {
        dailoguntils.showDialog(getContext());
        final UserMainActivity userMainActivity = (UserMainActivity) getActivity();
        final User user = userMainActivity.getUser();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        View v = getLayoutInflater().inflate(R.layout.dialog_pay,null);
        TextView tvUserName = v.findViewById(R.id.tv_user_name);
        final TextView tvPrice = v.findViewById(R.id.tv_numgber_price);
        final TextView tvPay = v.findViewById(R.id.tv_number_pay);
        final TextInputLayout txtILSeri = v.findViewById(R.id.input_seri);
        txtILSeri.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence!=null){
                    txtILSeri.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvPay.setText(Integer.toString(allPrice));
        tvPrice.setText(Integer.toString(allPrice));
        tvUserName.setText(takeUser().getFullName());
        alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dailoguntils.cancelDialog();
            }
        });
        alertDialog.setPositiveButton(getResources().getString(R.string.title_pay),null);

        alertDialog.setView(v);

        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        Button positive = dialog.getButton(dialog.BUTTON_POSITIVE);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtILSeri.getEditText().getText().toString().trim().equals("")) {
                    dailoguntils.cancelDialog();
                    txtILSeri.setError(getResources().getString(R.string.edt_emty));
                } else {
                    for (int j = 0; j < data.size(); j++) {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        ApiBuilder.getInstance().addtoorder(user.getUserName(), data.get(j).getImageLink(), data.get(j).getNameDrink(), data.get(j).getPrice() + "VND", 1, data.get(j).getNumberOrder(), format.format(calendar.getTime())).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                dailoguntils.cancelDialog();
                                if (response.code() == 200) {
                                    Toast.makeText(getContext(), "order success", Toast.LENGTH_SHORT).show();
                                    txtILSeri.setError(null);
                                    userMainActivity.getHistoryFragment().loadData();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getContext(), "order fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                dailoguntils.cancelDialog();
                            }
                        });
                    }
                }
            }
        });
    }
}
