package com.example.jon.fangreader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.app.App;
import com.example.jon.fangreader.base.BaseActivity;
import com.example.jon.fangreader.component.ImageLoader;
import com.example.jon.fangreader.di.component.DaggerActivityComponent;
import com.example.jon.fangreader.di.module.ActivityModule;
import com.example.jon.fangreader.model.bean.CommentListBean;
import com.example.jon.fangreader.model.bean.DiscussionDetailBean;
import com.example.jon.fangreader.presenter.BookDiscussionDetailPresenter;
import com.example.jon.fangreader.presenter.contract.BookDiscussionDetailContract;
import com.example.jon.fangreader.ui.adapter.BookDiscussionDetailAdapter;
import com.example.jon.fangreader.ui.adapter.BookDiscussionDetailHotCommentsAdapter;
import com.example.jon.fangreader.utils.DateUtil;
import com.example.jon.fangreader.widget.BookContentTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jon on 2017/2/15.
 */

public class BookDiscussionDetailActivity extends BaseActivity<BookDiscussionDetailPresenter> implements BookDiscussionDetailContract.View {

    public static final String DISCUSSION_ID = "discussion_id";

    @BindView(R.id.rv_discussiondetail_container)
    RecyclerView mRVContainer;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    private String mDiscussionId;
    private HeaderView mHeaderView;
    private BookDiscussionDetailAdapter mDiscussionDetailAdapter;
    private BookDiscussionDetailHotCommentsAdapter mHotCommentsAdapter;
    private List<CommentListBean.CommentsBean> mCommentsList;
    private List<CommentListBean.CommentsBean> mHotCommentsList;

    private int mStart;
    private int mLimit = 20;
    @Override
    protected void initDataAndEvent() {
        mDiscussionId = getIntent().getStringExtra(DISCUSSION_ID);
        //inittoolbar
        initToolBar();

        //hotList
        mHotCommentsList = new ArrayList<>();
        mHotCommentsAdapter = new BookDiscussionDetailHotCommentsAdapter(mHotCommentsList,mContext);
        mHeaderView = new HeaderView(LayoutInflater.from(mContext).inflate(R.layout.activity_discussiondetail_header,null,false));
        mHeaderView.rvHotCommentsContainer.setAdapter(mHotCommentsAdapter);
        mHeaderView.rvHotCommentsContainer.setLayoutManager(new LinearLayoutManager(mContext));

        //commentsList
        mCommentsList = new ArrayList<>();
        mDiscussionDetailAdapter = new BookDiscussionDetailAdapter(mContext, mCommentsList, mHeaderView, new BookDiscussionDetailAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                mStart = mStart + mLimit;
                mPresenter.getDiscussionComments(mDiscussionId,String.valueOf(mStart),String.valueOf(mLimit));
            }
        });
        mDiscussionDetailAdapter.setNoMore(false);
        mRVContainer.setAdapter(mDiscussionDetailAdapter);
        mRVContainer.setLayoutManager(new LinearLayoutManager(mContext));

        mProgressBar.setVisibility(View.VISIBLE);

        mPresenter.getDiscussionDetail(mDiscussionId);
        mPresenter.getBestComments(mDiscussionId);
        mPresenter.getDiscussionComments(mDiscussionId,String.valueOf(mStart),String.valueOf(mLimit));


    }

    private void initToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("详情");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_discussiondetail;
    }

    @Override
    protected void initInject() {
        DaggerActivityComponent.builder()
                .appComponent(App.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);

    }

    @Override
    public void setDiscussionDetail(DiscussionDetailBean bean) {
        mDiscussionDetailAdapter.cancelInIt();
        mProgressBar.setVisibility(View.GONE);
        ImageLoader.load(mContext,bean.getPost().getAuthor().getAvatar(),mHeaderView.ivPic);
        mHeaderView.tvName.setText(bean.getPost().getAuthor().getNickname());
        mHeaderView.tvTime.setText(DateUtil.formatTime(bean.getPost().getCreated()));
        mHeaderView.tvTitle.setText(bean.getPost().getTitle());
        mHeaderView.tvContent.setText(bean.getPost().getContent());
        mHeaderView.tvComments.setText("共"+bean.getPost().getCommentCount()+"条评论");
        mDiscussionDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void setBestComments(CommentListBean bestComments) {

        if(bestComments.getComments() == null || bestComments.getComments().size() == 0){
            mHeaderView.tvHotComments.setVisibility(View.GONE);
        }else {
            mHotCommentsList.addAll(bestComments.getComments());
            mHotCommentsAdapter.notifyDataSetChanged();
            mHeaderView.tvHotComments.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setDiscussionComments(CommentListBean commentListBean) {
        if(commentListBean.getComments() == null || commentListBean.getComments().size() == 0){
            mDiscussionDetailAdapter.setNoMore(true);
        }else {
            mDiscussionDetailAdapter.setNoMore(false);
            mCommentsList.addAll(commentListBean.getComments());
        }

        mDiscussionDetailAdapter.notifyDataSetChanged();

    }


    public class HeaderView extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_discussiondetail_pic)
        CircleImageView ivPic;
        @BindView(R.id.tv_discussiondetail_name)
        TextView tvName;
        @BindView(R.id.tv_discussiondetail_time)
        TextView tvTime;
        @BindView(R.id.tv_discussiondetail_title)
        TextView tvTitle;
        @BindView(R.id.tv_discussiondetail_content)
        BookContentTextView tvContent;
        @BindView(R.id.tv_discussiondetail_approve)
        TextView tvApprove;
        @BindView(R.id.iv_discussiondetail_share)
        ImageView ivShare;
        @BindView(R.id.iv_discussiondetail_more)
        ImageView ivMore;
        @BindView(R.id.tv_discussiondetail_hotcomment)
        TextView tvHotComments;
        @BindView(R.id.rv_discussiondetail_hotcomment_container)
        RecyclerView rvHotCommentsContainer;
        @BindView(R.id.tv_discussiondetail_comment)
        TextView tvComments;

        public HeaderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static void jumpToMe(Context context, String discussionId){
        Intent intent = new Intent(context,BookDiscussionDetailActivity.class);
        intent.putExtra(DISCUSSION_ID,discussionId);
        context.startActivity(intent);
    }
}
