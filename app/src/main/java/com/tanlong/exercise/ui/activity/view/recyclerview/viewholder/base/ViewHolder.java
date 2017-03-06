package com.tanlong.exercise.ui.activity.view.recyclerview.viewholder.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by 龙 on 2017/3/1.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    protected Context mContext;
    private View mConvertView;
    private SparseArray<View> mViews;

    public ViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    /**
     * 外部调用，获取ViewHolder实例
     * @param context -- 上下文
     * @param parent -- 父ViewGroup
     * @param layoutId -- 布局文件ID
     * @return
     */
    public static ViewHolder createViewHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(context, itemView);
    }

    /**
     * 外部调用，获取ViewHolder实例
     * @param context -- 上下文
     * @param itemView -- ItemView
     * @return
     */
    public static ViewHolder createViewHolder(Context context,  View itemView) {
        return new ViewHolder(context, itemView);
    }

    /**
     * 根据view id获取View
     * @param viewId -- View Id
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /*** 辅助方法 ***/
    /**
     * 设置TextView的值
     * @param viewId -- textView Id
     * @param content -- 设置内容
     */
    public ViewHolder setText(int viewId, String content) {
        TextView textView = getView(viewId);
        textView.setText(content);
        return this;
    }
}
