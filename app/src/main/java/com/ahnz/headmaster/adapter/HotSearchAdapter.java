package com.ahnz.headmaster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.activity_search.HotSearch;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author xzb
 * @description: 热门搜索 数据 适配器
 * @date :2020/7/9 17:06
 */
@Deprecated
public class HotSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HotSearch.DataBean> list;

    public HotSearchAdapter(List<HotSearch.DataBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_search, parent, false);
        ButterKnife.bind(this, view);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchViewHolder) {
            ((SearchViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.flow_tv)
        TextView hotSearch;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            hotSearch.setText(list.get(position).getName());
            itemView.setOnClickListener((view) -> {
            });
        }
    }
}
