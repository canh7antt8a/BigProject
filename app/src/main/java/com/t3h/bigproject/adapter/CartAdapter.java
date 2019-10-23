package com.t3h.bigproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.t3h.bigproject.R;
import com.t3h.bigproject.activity.UserMainActivity;
import com.t3h.bigproject.api.ApiBuilder;
import com.t3h.bigproject.model.Order;
import com.t3h.bigproject.model.User;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder>  {
    private LayoutInflater inflater;
    private ArrayList<Order> data;
    private itemOnclick listenner;
    private User user;
    public CartAdapter(Context context) {

        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Order> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setListenner(itemOnclick listenner) {
        this.listenner = listenner;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_cart,parent,false);
        return new CartHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartHolder holder, final int position) {
        holder.bindData(data.get(position));

        if (listenner!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenner.itemOnclickListener(position);
                }
            });
            holder.imgvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenner.imgvPlusOnclick(position);
                }
            });
            holder.imgvMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenner.imgvMinusOnclick(position);
                }
            });
            holder.imvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenner.itemMenuOnclickListener(position,view);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder{
        private TextView tvDrinkName;
        private TextView tvPrice;
        private TextView tvRateStar;
        private ImageView imgvPlus;
        private ImageView imgvMinus;
        private ImageView imvMenu;
        private EditText edtDrinkNumber;
        private TextView tvStatus;
        private ImageView imgvCartImage;

        public CartHolder(@NonNull View itemViewew){
            super(itemViewew);
            imvMenu = itemViewew.findViewById(R.id.imv_menu_item_cart);
            tvDrinkName = itemViewew.findViewById(R.id.tv_name_drink_item_cart);
            tvPrice = itemViewew.findViewById(R.id.tv_price_number_item_cart);
            tvRateStar = itemViewew.findViewById(R.id.tv_rate_number_rate_item_cart);
            imgvPlus = itemViewew.findViewById(R.id.imgv_plus_cart_item);
            imgvMinus = itemViewew.findViewById(R.id.imgv_minus_cart_item);
            edtDrinkNumber = itemViewew.findViewById(R.id.edt_number_drink_item_cart);
            tvStatus = itemViewew.findViewById(R.id.tv_status_item_cart);
            imgvCartImage = itemViewew.findViewById(R.id.imv_item_cart);
        }

        public void bindData(Order cart){
            tvDrinkName.setText(cart.getNameDrink());
            tvPrice.setText(cart.getPrice());
            tvRateStar.setText(String.valueOf(cart.getRate()));
            edtDrinkNumber.setText(String.valueOf(cart.getNumberOrder()));
            if (cart.getStatus()==1){
                tvStatus.setText(R.string.status_still_sell);
            }else if (cart.getStatus() == 2){
                tvStatus.setText(R.string.status_coming_soon);
            }else {
                tvStatus.setText(R.string.status_off_sell);
            }
            Glide.with(imgvCartImage)
                    .load(ApiBuilder.BASE_URL+cart.getImageLink())
                    .into(imgvCartImage);

        }

    }

    public interface itemOnclick{
        void imgvPlusOnclick(int position);
        void imgvMinusOnclick(int position);
        void itemOnclickListener(int position);
        void itemMenuOnclickListener(int postion,View v);
    }
}
