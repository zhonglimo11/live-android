package com.example.administrator.live.views;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.example.administrator.live.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.sun.resources.comment.CommendAdapter;
import com.sun.resources.comment.CustomLoadMoreView;


/**
 * @author Sun
 * @Date 2022/12/15
 * @Description 评论列表弹窗
 *
 * 此处可以使用 BottomSheetDialog  也可以用 BottomSheetDialogFragment 根据需求 怎么方便怎么来 就行 
 */
public class BgmDialog extends BottomSheetDialog {

    private static String[] tabNames = new String[] {"推荐", "收藏", "用过"};

    private Context mContext;
    private RecyclerView mRecyclerView;
    private CommendAdapter mAdapter;
    private FrameLayout layoutList;
    private OnListListener onListListener;

    public BgmDialog(@NonNull Context context, OnListListener onListListener) {
        super(context, R.style.CustomBottomSheetDialogTheme);
        setContentView(R.layout.my_bgm_dialog);
        this.mContext = context;
        this.onListListener = onListListener;
        initView();
    }

    private void initView() {
        layoutList = findViewById(R.id.layout_list);
        TabLayout tabLayout = findViewById(R.id.tabs1);

        //网络失败时 重试按钮
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//加载更多

        mAdapter = new CommendAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // 打开或关闭加载更多功能（默认为true）
        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
//        mAdapter.getLoadMoreModule().setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (onListListener != null) {
                    onListListener.getNextPage();
                }
            }
        });

        //设置空布局
        mAdapter.setEmptyView(R.layout.view_comment_empty);
        // 设置新的数据方法
    }




    public interface OnListListener {
        void getNextPage();

        void retryFirstPage();

        void getSecondList(int id, int page, int parentPosition);

        void comment(String content, int id, String beReplyId, String beReplyNickName, String beReplyAvatar, int parentPosition);

        void giveLikes(int type, int parentPosition, int position, int id, String upOrDown);

        void delComment(int type, int position, int parentPosition, int id);
    }
}
