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
import com.example.jon.fangreader.model.bean.ReviewListBean;
import com.example.jon.fangreader.model.bean.SortEvent;
import com.example.jon.fangreader.presenter.BookDetailCommunityReviewPresenter;
import com.example.jon.fangreader.presenter.contract.BookDetailCommunityReviewContract;
import com.example.jon.fangreader.ui.activity.BookReviewDetailActivity;
import com.example.jon.fangreader.ui.adapter.BookDetailCommunityReviewListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jon on 2017/2/13.
 */

public class ReviewFragment extends BaseFragment<BookDetailCommunityReviewPresenter> implements BookDetailCommunityReviewContract.View {

    public static final String BOOK_ID = "bookId";
    public static final String SORT = "sort";

    private String mBookId;
    private String mSort;
    private int mStart;
    private int mLimit = 20;
    private boolean mIsRefresh;

    @BindView(R.id.sr_bookdetailcommunity_review_refresh)
    SwipeRefreshLayout mSRefresh;
    @BindView(R.id.rv_bookdetailcommunity_review_container)
    RecyclerView mRVContainer;

    List<ReviewListBean.Reviews> mReviewsList;
    BookDetailCommunityReviewListAdapter mAdapter;



    public static Fragment newInstance(String bookId,String sort){
        Fragment fragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BOOK_ID,bookId);
        bundle.putString(SORT,sort);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInject() {
        DaggerFragmentComponent.builder()
                .appComponent(App.getAppComponent())
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookdetailcommunity_review;
    }

    @Override
    protected void initDataAndEvent() {
        Bundle bundle = getArguments();
        mBookId = bundle.getString(BOOK_ID);
        mSort = bundle.getString(SORT);
        mReviewsList = new ArrayList<>();
        mAdapter = new BookDetailCommunityReviewListAdapter(mContext, mReviewsList, new BookDetailCommunityReviewListAdapter.OnClickAndLoadListener() {
            @Override
            public void loadMore() {
                mStart = mStart + mLimit;
                mPresenter.getBookReviewList(mBookId,mSort,String.valueOf(mStart),String.valueOf(mLimit),mIsRefresh);
            }

            @Override
            public void onClick(int position) {

                BookReviewDetailActivity.jumpToMe(mContext,mReviewsList.get(position).getId());
            }
        });
        mAdapter.setNoMore(false);
        mRVContainer.setAdapter(mAdapter);
        mRVContainer.setLayoutManager(new LinearLayoutManager(mContext));
        mSRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSRefresh.setRefreshing(true);
                mStart = 0;
                mIsRefresh = true;
                mReviewsList.clear();
                mPresenter.getBookReviewList(mBookId,mSort,String.valueOf(mStart),String.valueOf(mLimit),mIsRefresh);

            }
        });
        mSRefresh.setRefreshing(true);
        mPresenter.getBookReviewList(mBookId,mSort,String.valueOf(mStart),String.valueOf(mLimit),mIsRefresh);


    }

    @Override
    public void setBookReviewList(ReviewListBean bean) {
        mSRefresh.setRefreshing(false);
        if(bean.getReviews() == null || bean.getReviews().size() == 0 ){
            mAdapter.setNoMore(true);
        }else {
            mReviewsList.addAll(bean.getReviews());
            mAdapter.setNoMore(false);
        }
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void receiveEvent(SortEvent event) {
        String temp = mSort;
        mSort = event.getSort();
        if(!temp.equals(mSort)){
            mStart = 0;
            mIsRefresh = false;
            mReviewsList.clear();
            mSRefresh.setRefreshing(true);
            mPresenter.getBookReviewList(mBookId,mSort,String.valueOf(mStart),String.valueOf(mLimit),mIsRefresh);
        }
    }
}
