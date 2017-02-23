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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.app.App;
import com.example.jon.fangreader.base.BaseActivity;
import com.example.jon.fangreader.component.ImageLoader;
import com.example.jon.fangreader.di.component.DaggerActivityComponent;
import com.example.jon.fangreader.di.module.ActivityModule;
import com.example.jon.fangreader.model.bean.CommentListBean;
import com.example.jon.fangreader.model.bean.ReviewDetailBean;
import com.example.jon.fangreader.presenter.BookReviewDetailPresenter;
import com.example.jon.fangreader.presenter.contract.BookReviewDetailContract;
import com.example.jon.fangreader.ui.adapter.BookDiscussionDetailAdapter;
import com.example.jon.fangreader.ui.adapter.BookDiscussionDetailHotCommentsAdapter;
import com.example.jon.fangreader.utils.DateUtil;
import com.example.jon.fangreader.widget.BookContentTextView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jon on 2017/2/17.
 */

public class BookReviewDetailActivity extends BaseActivity<BookReviewDetailPresenter> implements BookReviewDetailContract.View {
    public static final String ID = "id";

    @BindView(R.id.rv_discussiondetail_container)
    RecyclerView mRVContainer;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    private String mReviewId;
    private int mStart;
    private int mLimit = 20;

    private List<CommentListBean.CommentsBean> mHotCommentsList;
    private List<CommentListBean.CommentsBean> mReviewCommentsList;

    private BookDiscussionDetailAdapter mCommentsAdapter;
    private BookDiscussionDetailHotCommentsAdapter mHotCommentsAdapter;

    private HeaderView mHeader;


    public static void jumpToMe(Context context,String id){
        Intent intent = new Intent(context,BookReviewDetailActivity.class);
        intent.putExtra(ID,id);
        context.startActivity(intent);
    }

    @Override
    protected void initDataAndEvent() {
        mReviewId = getIntent().getStringExtra(ID);
        Logger.e(mReviewId);
        initToolBar();

        //hotList
        mHotCommentsList = new ArrayList<>();
        mHotCommentsAdapter = new BookDiscussionDetailHotCommentsAdapter(mHotCommentsList,mContext);
        mHeader = new HeaderView(LayoutInflater.from(mContext).inflate(R.layout.activity_reviewdetail_header,null,false));
        mHeader.rvHotCommentsContainer.setAdapter(mHotCommentsAdapter);
        mHeader.rvHotCommentsContainer.setLayoutManager(new LinearLayoutManager(mContext));

        //commentList
        mReviewCommentsList = new ArrayList<>();
        mCommentsAdapter = new BookDiscussionDetailAdapter(mContext, mReviewCommentsList, mHeader, new BookDiscussionDetailAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                mStart = mStart + mLimit;
                mPresenter.getReviewComments(mReviewId,String.valueOf(mStart),String.valueOf(mLimit));

            }
        });
        mCommentsAdapter.setNoMore(false);

        mRVContainer.setAdapter(mCommentsAdapter);
        mRVContainer.setLayoutManager(new LinearLayoutManager(mContext));

        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.getReviewDetail(mReviewId);
        mPresenter.getBestComments(mReviewId);
        mPresenter.getReviewComments(mReviewId,String.valueOf(mStart),String.valueOf(mLimit));


    }

    private void initToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("书评详情");
        mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
    public void setReviewDetail(final ReviewDetailBean bean) {
        mProgressBar.setVisibility(View.GONE);
        mCommentsAdapter.cancelInIt();
        ImageLoader.load(mContext,bean.getReview().getAuthor().getAvatar(),mHeader.ivPic);
        mHeader.tvName.setText(bean.getReview().getAuthor().getNickname());
        mHeader.tvTime.setText(DateUtil.formatTime(bean.getReview().getCreated()));
        mHeader.tvTitle.setText(bean.getReview().getTitle());
        mHeader.tvContent.setText(bean.getReview().getContent());
        ImageLoader.load(mContext,bean.getReview().getBook().getCover(),mHeader.ivBookPic);
        mHeader.tvBookName.setText(bean.getReview().getBook().getTitle());
        mHeader.rbBookAssess.setProgress(bean.getReview().getRating());
        mHeader.tvUseful.setText(String.valueOf(bean.getReview().getHelpful().getYes()));
        mHeader.tvNoUseful.setText(String.valueOf(bean.getReview().getHelpful().getNo()));
        mHeader.tvComment.setText("共"+bean.getReview().getCommentCount()+"条评论");
        mHeader.rlBookAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookDetailActivity.jumpToMe(mContext,bean.getReview().getBook().getId());
            }
        });
        mCommentsAdapter.notifyDataSetChanged();

    }

    @Override
    public void setBestComments(CommentListBean commentListBean) {
        mProgressBar.setVisibility(View.GONE);
        if(commentListBean.getComments() == null || commentListBean.getComments().size() == 0){
            mHeader.tvHotComment.setVisibility(View.GONE);
        }else {
            mHeader.tvHotComment.setVisibility(View.VISIBLE);
            mHotCommentsList.addAll(commentListBean.getComments());
            mHotCommentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setReviewComments(CommentListBean commentListBean) {
        mProgressBar.setVisibility(View.GONE);
        if(commentListBean.getComments() == null || commentListBean.getComments().size() == 0){
            mCommentsAdapter.setNoMore(true);
        }else {
            mCommentsAdapter.setNoMore(false);
            mReviewCommentsList.addAll(commentListBean.getComments());
        }
        mCommentsAdapter.notifyDataSetChanged();
    }

    class HeaderView extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_reviewdetail_pic)
        CircleImageView ivPic;
        @BindView(R.id.tv_reviewdetail_name)
        TextView tvName;
        @BindView(R.id.tv_reviewdetail_time)
        TextView tvTime;
        @BindView(R.id.tv_reviewdetail_title)
        TextView tvTitle;
        @BindView(R.id.tv_reviewdetail_content)
        BookContentTextView tvContent;
        @BindView(R.id.iv_reviewdetail_bookpic)
        ImageView ivBookPic;
        @BindView(R.id.tv_reviewdetail_bookname)
        TextView tvBookName;
        @BindView(R.id.rb_reviewdetail_assess)
        RatingBar rbBookAssess;
        @BindView(R.id.iv_reviewdetail_more)
        ImageView ivMore;
        @BindView(R.id.iv_reviewdetail_share)
        ImageView ivShare;
        @BindView(R.id.tv_reviewdetail_useful)
        TextView tvUseful;
        @BindView(R.id.tv_reviewdetail_nouseful)
        TextView tvNoUseful;
        @BindView(R.id.rv_reviewdetail_hotcomment_container)
        RecyclerView rvHotCommentsContainer;
        @BindView(R.id.tv_reviewdetail_comment)
        TextView tvComment;
        @BindView(R.id.tv_reviewdetail_hotcomment)
        TextView tvHotComment;
        @BindView(R.id.rl_reviewdetail_bookassess)
        RelativeLayout rlBookAssess;

        public HeaderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
