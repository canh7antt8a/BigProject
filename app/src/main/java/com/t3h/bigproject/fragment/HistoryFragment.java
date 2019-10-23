package com.t3h.bigproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.bigproject.R;
import com.t3h.bigproject.activity.UserMainActivity;
import com.t3h.bigproject.adapter.HistoryAdapter;
import com.t3h.bigproject.api.ApiBuilder;
import com.t3h.bigproject.model.History;
import com.t3h.bigproject.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {
    private HistoryAdapter adapter;
    private RecyclerView rcvHistory;
    private ArrayList<History> data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadData();
    }

    public void loadData() {
        UserMainActivity userMainActivity = (UserMainActivity) getActivity();
        User user = userMainActivity.getUser();
        ApiBuilder.getInstance().getHistory(user.getUserName()).enqueue(new Callback<ArrayList<History>>() {
            @Override
            public void onResponse(Call<ArrayList<History>> call, Response<ArrayList<History>> response) {
                data = response.body();
                adapter.setData(data);
                rcvHistory.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<History>> call, Throwable t) {

            }
        });
    }

    private void initView() {
        adapter = new HistoryAdapter(getContext());
        rcvHistory = getActivity().findViewById(R.id.rcv_history);
    }
}
