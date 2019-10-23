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
import com.t3h.bigproject.api.ApiBuilder;
import com.t3h.bigproject.model.History;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private LayoutInflater inflater;
    private ArrayList<History> data;

    public HistoryAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<History> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_history,parent,false);
        return new HistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder{
        private TextView tvNameDrink;
        private TextView tvPrice;
        private TextView tvDate;
        private EditText edtNumberDrink;
        private ImageView imvBackGround;
        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            imvBackGround = itemView.findViewById(R.id.imv_item_history);
            tvNameDrink = itemView.findViewById(R.id.tv_name_drink_item_history);
            tvPrice = itemView.findViewById(R.id.tv_price_number_item_history);
            tvDate = itemView.findViewById(R.id.tv_date_item_history);
            edtNumberDrink = itemView.findViewById(R.id.edt_number_drink_item_history);
        }

        public void bindData(History history){
            Glide.with(imvBackGround).load(ApiBuilder.BASE_URL+history.getImageLink()).into(imvBackGround);
            tvNameDrink.setText(history.getNameDrink());
            tvPrice.setText(history.getPrice());
            tvDate.setText(history.getDate());
            edtNumberDrink.setText(String.valueOf(history.getNumberDrink()));
        }
    }

}
