package com.tanlong.exercise.ui.activity.ipc.aidl;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanlong.exercise.R;
import com.tanlong.exercise.aidl.Book;
import com.tanlong.exercise.model.event.AddBookEvent;
import com.tanlong.exercise.model.event.SetBookEvent;
import com.tanlong.exercise.service.AIDLService;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.ui.activity.view.listview.adapter.BookAdapter;
import com.tanlong.exercise.ui.fragment.dialog.ShowTipsFragment;
import com.tanlong.exercise.util.ToastHelp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 龙 on 2017/4/10.
 */

public class AIDLIPCActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_help)
    Button btnHelp;
    @Bind(R.id.et_reply)
    EditText etReply;
    @Bind(R.id.btn_confirm_book)
    Button btnConfirmReply;
    @Bind(R.id.lv_book)
    ListView lvBook;

    BookAdapter mAdapter;
    List<Book> mListBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aidl_ipc);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mListBook = new ArrayList<>();

        startBookAIDLService();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        btnHelp.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.aidl_ipc);
        mAdapter = new BookAdapter(this, mListBook, R.layout.item_book_list);
        lvBook.setAdapter(mAdapter);
    }

    private void startBookAIDLService() {
        Intent intent = new Intent(this, AIDLService.class);
        startService(intent);
    }

    @OnClick({R.id.iv_back, R.id.btn_help, R.id.btn_confirm_book})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_help:
                showTips();
                break;
            case R.id.btn_confirm_book:
                setBook();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddBookEvent(AddBookEvent event) {
        mListBook.add(event.getData());
        mAdapter.notifyDataSetChanged();
    }

    private void setBook() {
        String price = etReply.getText().toString();
        if (TextUtils.isEmpty(price)) {
            ToastHelp.showShortMsg(this, "请输入价格");
            return;
        }
        Book book = new Book();
        book.setPrice(Integer.valueOf(price));
        new SetBookEvent().setData(book).post();
    }

    private void showTips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("详见已收藏文章“Android：学习AIDL，这一篇文章就够了(上)");

        ShowTipsFragment fragment = ShowTipsFragment.newInstance(stringBuilder.toString());
        fragment.show(getSupportFragmentManager(), "");
    }
}
