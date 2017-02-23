package com.example.jon.fangreader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.app.App;
import com.example.jon.fangreader.base.BaseFragment;
import com.example.jon.fangreader.di.component.DaggerFragmentComponent;
import com.example.jon.fangreader.di.module.FragmentModule;
import com.example.jon.fangreader.model.bean.DiscussionListBean;
import com.example.jon.fangreader.model.bean.SortEvent;
import com.example.jon.fangreader.presenter.BookDetailCommunityDiscussionPresenter;
import com.example.jon.fangreader.presenter.contract.BookDetailCommunityDiscussionContract;
import com.example.jon.fangreader.ui.activity.BookDiscussionDetailActivity;
import com.example.jon.fangreader.ui.adapter.BookDetailCommunityDiscussionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jon on 2017/2/13.
 */

public class DiscussFragment extends BaseFragment<BookDetailCommunityDiscussionPresenter> implements BookDetailCommunityDiscussionContract.View {

    public static final String BOOK_ID = "bookId";
    public static final String SORT = "sort";


    @BindView(R.id.sr_bookdetailcommunity_discussion_refresh)
    SwipeRefreshLayout mSRRefresh;
    @BindView(R.id.rv_bookdetailcommunity_discussion_container)
    RecyclerView mRVContainer;

    BookDetailCommunityDiscussionAdapter mAdapter;

    private String mSort;
    private String mBookId;
    private int mStart;
    private int mLimit = 20;
    private List<DiscussionListBean.PostBean> mPostBeans;

    private boolean mHasRefresh;

    public static Fragment newInstance(String bookId,String sort) {
        Fragment fragment = new DiscussFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BOOK_ID,bookId);
        bundle.putString(SORT,sort);
        fragment.setArguments(bundle);
        return fragment;

    }
    @Override
    protected void initInject() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .appComponent(App.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookdetailcommunity_discussion;
    }

    @Override
    protected void initDataAndEvent() {
        Bundle bundle = getArguments();
        mBookId = bundle.getString(BOOK_ID);
        mSort = bundle.getString(SORT);
        mSRRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHasRefresh = true;
                mSRRefresh.setRefreshing(true);
                mPostBeans.clear();
                mStart = 0;
                mPresenter.getBookDetailDiscussionList(mBookId,mSort,mStart,mLimit,true);
            }
        });
        mPostBeans = new ArrayList<>();
        mAdapter = new BookDetailCommunityDiscussionAdapter(mContext, mPostBeans, mRVContainer, new BookDetailCommunityDiscussionAdapter.ClickAndLoadListener() {
            @Override
            public void loadMore() {
                mStart = mStart + mLimit;
                mPresenter.getBookDetailDiscussionList(mBookId,mSort,mStart,mLimit,mHasRefresh);
            }

            @Override
            public void onClick(int position) {
                BookDiscussionDetailActivity.jumpToMe(mContext,mPostBeans.get(position).getId());
            }
        });
        mAdapter.setNoMore(false);
        mRVContainer.setAdapter(mAdapter);
        mRVContainer.setLayoutManager(new LinearLayoutManager(mContext));
        mSRRefresh.setRefreshing(true);
        mPresenter.getBookDetailDiscussionList(mBookId,mSort,mStart,mLimit,false);


    }

    @Override
    public void setBookDetailDiscussionList(DiscussionListBean discussionList) {
        mSRRefresh.setRefreshing(false);
        if(discussionList.getPosts().size() == 0){
            mAdapter.setNoMore(true);
        }else {
            mAdapter.setNoMore(false);
            mPostBeans.addAll(discussionList.getPosts());
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void receiveEvent(SortEvent event) {
        String temp = mSort;
        mSort = event.getSort();
        if(!temp.equals(mSort)){
            mStart = 0;
            mPostBeans.clear();
            mHasRefresh = false;
            mPresenter.getBookDetailDiscussionList(mBookId,mSort,mStart,mLimit,mHasRefresh);

        }
    }
}
