package com.tanlong.exercise.ui.activity.view.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanlong.exercise.R;
import com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.SimpleViewHolder;
import com.tanlong.exercise.util.LogTool;

import java.util.List;

/**
 *
 * Created by 龙 on 2017/2/20.
 */

public class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    private List<String> mDatas;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public SimpleRecyclerViewAdapter(Context context, List<String> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        holder.getTvContent().setText(mDatas.get(position));
        LogTool.e("test", position + "width is " + holder.getTvContent().getWidth());

        if (mOnItemClickListener != null) {
            holder.getTvContent().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(v, holder.getLayoutPosition());
                }
            });

            holder.getTvContent().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(v, holder.getLayoutPosition());
                    return true;//不再触发单击事件
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void addData(int position, String content) {
        //判断是否可以在该位置添加
        if (position < getItemCount() && position >= 0) {//在size内
            mDatas.add(position, content);
        } else if (position >= getItemCount()) {//超过size，放在最后
            position = getItemCount() - 1;
            if (position < 0) {
                position = 0;
            }
            mDatas.add(position, content);
        } else {
            position = 0;
            mDatas.add(position, content);
        }
        notifyItemInserted(position);

    }

    public void removeData(int position) {
        if (position < getItemCount() && position >= 0) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }
    }
}
