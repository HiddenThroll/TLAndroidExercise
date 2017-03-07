package com.tanlong.exercise.ui.activity.view.recyclerview.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tanlong.exercise.R;

/**
 * SimpleAdapter对应的ViewHolder
 * Created by 龙 on 2017/2/20.
 */

public class SimpleViewHolder extends RecyclerView.ViewHolder{

    TextView tvContent;

    public SimpleViewHolder(View itemView) {
        super(itemView);
        tvContent = (TextView) itemView.findViewById(R.id.tv_recycler_content);
    }

    public TextView getTvContent() {
        return tvContent;
    }
}
