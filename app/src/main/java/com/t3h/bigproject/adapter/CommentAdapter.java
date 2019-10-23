package com.t3h.bigproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.t3h.bigproject.R;
import com.t3h.bigproject.api.ApiBuilder;
import com.t3h.bigproject.model.Comment;
import com.t3h.bigproject.model.UserImage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private ArrayList<Comment> data;
    private LayoutInflater inflater;

    public CommentAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Comment> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.comment_item,parent,false);
        return new CommentHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data ==null?0:data.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder{
        private ImageView cimvComment;
        private TextView tvUserName;
        private RatingBar rbComment;
        private TextView tvComment;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            cimvComment = itemView.findViewById(R.id.cimg_item_comment);
            tvUserName = itemView.findViewById(R.id.tv_username_item_comment);
            rbComment = itemView.findViewById(R.id.rb_your_rating_comment_item);
            tvComment = itemView.findViewById(R.id.tv_comment_item_comment);
        }

        public void bindData(Comment comment){
            ApiBuilder.getInstance().getImageUser(comment.getUserName()).enqueue(new Callback<UserImage>() {
                @Override
                public void onResponse(Call<UserImage> call, Response<UserImage> response) {
                    UserImage userImage = response.body();
                    if (response.code()==200){
                        Glide.with(cimvComment).load(ApiBuilder.BASE_URL+userImage.getAvatar()).into(cimvComment);
                    }
                }

                @Override
                public void onFailure(Call<UserImage> call, Throwable t) {

                }
            });

            tvUserName.setText(comment.getUserName());
            tvComment.setText(comment.getComment());
            rbComment.setRating(comment.getRate());
        }
    }
}
