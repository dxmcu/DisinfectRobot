package com.linfd.scri.disinfectrobot.view.recyclerviewUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

/**
 * https://blog.csdn.net/bsmmaoshenbo/article/details/68066460
 * Created by wangjitao on 2016/11/7 0007.
 * 对简单的recycleview进行简单的封装
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<T> datas;
    private int layoutId;
    protected OnItemClickListner onItemClickListner; //单击事件
    protected OnItemLongClickListner onItemLongClickListner; //长按单击事件
    private boolean clickFlag = true ; //单击事件和长单击事件的屏蔽标识

    public BaseRecyclerViewAdapter(Context context, List<T> datas, int layoutId) {
        this .context = context;
        this .datas = datas;
        this .layoutId = layoutId;
        this .inflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = new BaseViewHolder(inflater.inflate(layoutId, parent, false ));

        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        bindData(holder, datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    protected abstract void bindData(BaseViewHolder holder, T data, int position);

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this .onItemClickListner = onItemClickListner;
    }

    public void setOnItemLongClickListner(OnItemLongClickListner onItemLongClickListner) {
        this .onItemLongClickListner = onItemLongClickListner;
    }

    public interface OnItemClickListner {
        void onItemClickListner(View v, int position);
    }

    public interface OnItemLongClickListner {
        void onItemLongClickListner(View v, int position);
    }
}
