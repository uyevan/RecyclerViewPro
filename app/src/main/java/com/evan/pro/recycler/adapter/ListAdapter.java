package com.evan.pro.recycler.adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.evan.pro.recycler.R;
import com.evan.pro.recycler.holder.ItemViewHolder;
import com.evan.pro.recycler.model.Item;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    /**
     * 数据集 itemList
     */
    private List<Item> itemList;

    /**
     * 子项View视图
     */
    private View view;

    /**
     * 子项Item模型
     */
    private Item item;

    private ListAdapter() {
    }

    public ListAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    /**
     * 构造View视图缓存器ItemViewHolder
     *
     * @param parent   父视图
     * @param viewType 视图类型
     * @return
     */
    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO 创建缓存器并传递给一个View视图
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        /*view = View.inflate(parent.getContext(), R.layout.item_list, null); 通过View.inflate无法宽度最大化*/
        return new ItemViewHolder(view);
    }

    /**
     * 绑定数据到ItemViewHolder视图缓存器的每一个Item
     *
     * @param holder   子项Item对应的View视图
     * @param position 子项Item在数据集里的位置
     */
    @Override
    @SuppressLint("DefaultLocale")
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //TODO 获取第 position 个子项
        item = itemList.get(position);
        //TODO 设置Holder对应的视图数据
        holder.id.setText(String.valueOf(item.getId()));
        holder.name.setText(item.getName());
        Glide.with(view.getContext()).load(item.getHead()).into(holder.head);
        holder.profile.setText(String.format("%s | POSITION:%d", item.getProfile(), position));
    }

    /**
     * 数据集里面的总项数
     *
     * @return itemList.size() - 1
     */
    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }
}
