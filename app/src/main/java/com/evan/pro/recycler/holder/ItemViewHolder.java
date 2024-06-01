package com.evan.pro.recycler.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evan.pro.recycler.R;

/**
 * RecyclerView子项Item的缓存器
 *
 * @apiNote <code>RecyclerView.ViewHolder</code>
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {

    public final TextView id, name, profile;
    public final ImageView head;

    /**
     * Item构造器
     *
     * @param itemView
     */
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        //TODO 绑定元素
        id = itemView.findViewById(R.id.id);
        name = itemView.findViewById(R.id.name);
        head = itemView.findViewById(R.id.head);
        profile = itemView.findViewById(R.id.profile);
    }

}
