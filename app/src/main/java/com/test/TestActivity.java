package com.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fxchat.com.nestscrolledpulltorecycler.R;
import fxchat.com.nestscrolledpulltorecycler.refreshui.PullToRefreshBase;
import fxchat.com.nestscrolledpulltorecycler.refreshui.PullToRefreshRecyclerView;

/**
 * Created by wenjiarong on 2019/4/4 0004.
 */
public class TestActivity extends FragmentActivity {
    SimpleAdapter simpleAdapter;
    int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullto_refresh);
//        RecyclerView recyclerView = findViewById(R.id.recycler);
        final PullToRefreshRecyclerView pullToRefreshRecyclerView = findViewById(R.id.recycler);
        RecyclerView recyclerView = pullToRefreshRecyclerView.getRefreshableView();
        pullToRefreshRecyclerView.setPullLoadEnabled(true);
        pullToRefreshRecyclerView.setPullRefreshEnabled(true);
        pullToRefreshRecyclerView.getRefreshableView().setNestedScrollingEnabled(true);
        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<MyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<MyRecyclerView> refreshView) {
                List<String> data = new ArrayList<>();
                page = 0;
                for (int i = 0; i < 20; i++) {
                    data.add(String.valueOf(i + page * 10));
                }
                page++;
                simpleAdapter.setData(data);
                pullToRefreshRecyclerView.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<MyRecyclerView> refreshView) {
                List<String> data = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    data.add(String.valueOf(i + page * 10));
                }
                page++;
                simpleAdapter.addData(data);
                pullToRefreshRecyclerView.onPullUpRefreshComplete();
            }
        });
        simpleAdapter = new SimpleAdapter();
        recyclerView.setAdapter(simpleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public static class SimpleAdapter extends RecyclerView.Adapter<SimpleViewHolder> {
        List<String> data = new ArrayList<>();

        public SimpleAdapter() {
            for (int i = 0; i < 100; i++) {
                data.add(String.valueOf(i));
            }
            notifyDataSetChanged();
        }

        public void setData(List<String> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        public void addData(List<String> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new SimpleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder simpleViewHolder, int i) {
            simpleViewHolder.onBind(data.get(i));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView dataText;

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            dataText = itemView.findViewById(android.R.id.text1);
        }

        public void onBind(String s) {
            dataText.setText(s);
        }
    }
}
