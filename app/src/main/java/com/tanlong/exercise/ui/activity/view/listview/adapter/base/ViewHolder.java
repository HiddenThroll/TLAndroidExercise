package com.tanlong.exercise.ui.activity.view.listview.adapter.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *
 * Created by Administrator on 2016/3/14.
 */
public class ViewHolder {
    private final SparseArray<View> mViews;
    private View mConvertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {
        this.mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);// setTag
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context     -- 上下文
     * @param convertView -- 复用View
     * @param parent      -- 父布局
     * @param layoutId    -- 布局文件ID
     * @param position    -- 位置
     * @return
     */
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {

        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }

    /**
     * 通过控件的Id获取对应的控件，如果没有则加入views
     *
     * @param viewId -- 控件ID
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

    public void setText(int id, String content) {
        TextView textView = getView(id);
        textView.setText(content);
    }
}
