package com.t3h.bigproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.t3h.bigproject.R;
import com.t3h.bigproject.api.ApiBuilder;
import com.t3h.bigproject.model.Order;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private LayoutInflater inflater;
    private ArrayList<Order> data;
    private itemListenner listenner;

    public void setListenner(itemListenner listenner) {
        this.listenner = listenner;
    }

    public OrderAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Order> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_drink,parent,false);
        return new OrderHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, final int position) {
        holder.bindData(data.get(position),inflater.getContext());
        if (listenner!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenner.itemOnclickListener(position);
                }
            });

            holder.imvCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenner.imvCartOnClickListennet(position);
                }
            });

            holder.imvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenner.imvMenuOnClickListenner(position,view);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        private ImageView imvImageOrder;
        private TextView tvNameDrink;
        private TextView tvPrice;
        private RatingBar rbRateDrink;
        private TextView tvNumberComment;
        private TextView tvNumberRate;
        private TextView tvStatus;
        private ImageView imvMenu;
        private ImageView imvCart;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            imvImageOrder = itemView.findViewById(R.id.imv_item_order);
            imvMenu = itemView.findViewById(R.id.imv_menu_item_order);
            imvCart = itemView.findViewById(R.id.imv_cart_item_order);
            tvNameDrink = itemView.findViewById(R.id.tv_name_drink_order_item);
            tvPrice = itemView.findViewById(R.id.tv_price_number_item_order);
            rbRateDrink = itemView.findViewById(R.id.rb_item_order);
            tvNumberComment = itemView.findViewById(R.id.tv_number_comment_item_order);
            tvNumberRate = itemView.findViewById(R.id.tv_rate_number_rate_order_item);
            tvStatus = itemView.findViewById(R.id.tv_status_item_drink);
        }

        public void bindData(Order order,Context context){
            Glide.with(imvImageOrder)
                    .load(ApiBuilder.BASE_URL+order.getImageLink())
                    .into(imvImageOrder);
            tvNameDrink.setText(order.getNameDrink());
            tvPrice.setText(order.getPrice());
            rbRateDrink.setRating(order.getRate());
            tvNumberComment.setText(order.getNumberComment());
            tvNumberRate.setText(String.valueOf(order.getRate()));
            String status = "";
            if (order.getStatus()==1){
                status = context.getResources().getString(R.string.status_still_sell);
            }else if (order.getStatus()==2){
                status = context.getResources().getString(R.string.status_coming_soon);
                tvStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }else {
                status = context.getResources().getString(R.string.status_off_sell);
                tvStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
            tvStatus.setText(status);
        }
    }

    public interface itemListenner{
        void itemOnclickListener(int position);
        void imvMenuOnClickListenner(int position,View v);
        void imvCartOnClickListennet(int position);
    }
}
