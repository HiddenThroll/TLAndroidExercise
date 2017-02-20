package com.tanlong.exercise.ui.activity.view.listview.adapter;

import android.content.Context;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.model.entity.IMenuItem;
import com.tanlong.exercise.ui.activity.view.listview.adapter.base.CommonAdapter;
import com.tanlong.exercise.ui.activity.view.listview.adapter.base.ViewHolder;

import java.util.List;

/**
 * 菜单Adapter
 * Created by 龙 on 2016/6/28.
 */
public class MenuAdapter extends CommonAdapter<IMenuItem> {

    public MenuAdapter(Context context, List<IMenuItem> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, IMenuItem item, int position) {
        TextView tvName = helper.getView(R.id.tv_menu_name);
        tvName.setText(item.getName());
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getMenuType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
}
