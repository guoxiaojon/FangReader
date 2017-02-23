package com.example.jon.fangreader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.app.App;
import com.example.jon.fangreader.base.BaseActivity;
import com.example.jon.fangreader.component.ImageLoader;
import com.example.jon.fangreader.di.component.DaggerActivityComponent;
import com.example.jon.fangreader.di.module.ActivityModule;
import com.example.jon.fangreader.model.bean.BookDetailBean;
import com.example.jon.fangreader.model.bean.HotReviewBean;
import com.example.jon.fangreader.model.bean.RecommendBean;
import com.example.jon.fangreader.model.bean.RecommendBookList;
import com.example.jon.fangreader.presenter.BookDetailPresenter;
import com.example.jon.fangreader.presenter.contract.BookDetailContract;
import com.example.jon.fangreader.ui.adapter.BookDetailHotReviewAdapter;
import com.example.jon.fangreader.ui.adapter.BookDetailRecommdBooksAdapter;
import com.example.jon.fangreader.utils.CollectionUtil;
import com.example.jon.fangreader.utils.DateUtil;
import com.example.jon.fangreader.utils.SystemUtils;
import com.example.jon.fangreader.utils.TextUtil;
import com.example.jon.fangreader.widget.DrawableCenterButton;
import com.example.jon.fangreader.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jon on 2017/2/20.
 */

public class BookDetailActivity extends BaseActivity<BookDetailPresenter> implements BookDetailContract.View {
    public static final String ID = "bookId";

    private String mBookId;

    private List<RecommendBookList.RecommendBook> mRecommendBookList;
    private List<HotReviewBean.Reviews> mHotReviewList;
    private BookDetailHotReviewAdapter mHotReviewAdapter;
    private BookDetailRecommdBooksAdapter mRecommendBookAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.iv_bookdetail_pic)
    ImageView mIVPic;
    @BindView(R.id.tv_bookdetail_title)
    TextView mTVTitle;
    @BindView(R.id.tv_bookdetail_name)
    TextView mTVName;
    @BindView(R.id.tv_bookdetail_sort)
    TextView mTVCat;
    @BindView(R.id.tv_bookdetail_wordnum)
    TextView mTVWordCount;
    @BindView(R.id.tv_bookdetail_time)
    TextView mTVTime;
    @BindView(R.id.tv_bookdetail_subbutton)
    DrawableCenterButton mTVSubBook;
    @BindView(R.id.tv_bookdetail_readbutton)
    DrawableCenterButton mTVReadBook;
    @BindView(R.id.tv_bookdetail_peoplenum)
    TextView mTVLatelyFollower;
    @BindView(R.id.tv_bookdetail_peopleleft)
    TextView mTVRetentionRatio;
    @BindView(R.id.tv_bookdetail_updatenum)
    TextView mTVSerializeWordCount;
    @BindView(R.id.fl_bookdetail_taggroup)
    FlowLayout mTagGroup;
    @BindView(R.id.tv_bookdetail_content)
    TextView mTVContent;
    @BindView(R.id.rv_bookdetail_reviewcontainer)
    RecyclerView mRVHotReviewContainer;
    @BindView(R.id.tv_bookdetail_community)
    TextView mTVCommTag;
    @BindView(R.id.tv_bookdetail_numofdiscussion)
    TextView mTVPostNum;
    @BindView(R.id.rv_bookdetail_recommendbookcontainer)
    RecyclerView mRVRecommendBooksContainer;
    @BindView(R.id.ll_bookdetail_bg)
    LinearLayout mLLBg;

    private List<String> mTags;

    private RecommendBean.Book mBook;



    public static void jumpToMe(Context context,String bookId){
        Intent intent = new Intent(context,BookDetailActivity.class);
        intent.putExtra(ID,bookId);
        context.startActivity(intent);
    }

    @Override
    protected void initDataAndEvent() {
        mBookId = getIntent().getStringExtra(ID);
        mBook = new RecommendBean.Book();
        ininToolBar();
        mTags = new ArrayList<>();
        mHotReviewList = new ArrayList<>();
        mRecommendBookList = new ArrayList<>();
        mHotReviewAdapter = new BookDetailHotReviewAdapter(mHotReviewList, mContext, new BookDetailHotReviewAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                BookReviewDetailActivity.jumpToMe(mContext,mHotReviewList.get(position).getId());
            }
        });
        mRecommendBookAdapter = new BookDetailRecommdBooksAdapter(mRecommendBookList, mContext, new BookDetailRecommdBooksAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                //goto
                BookListDetailActivity.jumpToMe(mContext,mRecommendBookList.get(position));
            }
        });
        mRVHotReviewContainer.setAdapter(mHotReviewAdapter);
        mRVHotReviewContainer.setLayoutManager(new LinearLayoutManager(mContext));
        mRVRecommendBooksContainer.setAdapter(mRecommendBookAdapter);
        mRVRecommendBooksContainer.setLayoutManager(new LinearLayoutManager(mContext));


        mPresenter.getBookDetail(mBookId);
        mPresenter.getHotReview(mBookId);
        mPresenter.getRecommendBookList(mBookId,"3");



    }

    private void ininToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("书籍详情");
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
        return R.layout.activity_bookdetail;
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
    public void setBookDetail(BookDetailBean bookDetail) {
        ImageLoader.load(mContext,bookDetail.getCover(),mIVPic);
        mTVTitle.setText(bookDetail.getTitle());
        mTVName.setText(bookDetail.getAuthor());
        mTVName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //搜索名字
            }
        });
        mTVCat.setText(" | "+bookDetail.getCat()+" | ");
        mTVWordCount.setText(TextUtil.formatNum(bookDetail.getWordCount()));
        mTVTime.setText(DateUtil.formatTime(bookDetail.getUpdated()));

        mBook.setId(mBookId);
        mBook.setTitle(bookDetail.getTitle());
        mBook.setCover(bookDetail.getCover());
        mBook.setLastChapter(bookDetail.getLastChapter());
        mBook.setUpdated(bookDetail.getUpdated());

        boolean tag = CollectionUtil.getCollectionList().contains(mBook);
        mTVSubBook.setTag(tag);
        if(tag){
            mTVSubBook.setBackground(getResources().getDrawable(R.drawable.button_gray_bg));
            Drawable drawable = getResources().getDrawable(R.mipmap.book_detail_info_del_img);
            drawable.setBounds(0,0,15,15);
            mTVSubBook.setText("不追了");
            mTVSubBook.setCompoundDrawables(drawable,null,null,null);
        }
        mTVSubBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((Boolean) mTVSubBook.getTag()){
                    mTVSubBook.setTag(false);
                    CollectionUtil.deleteCollectionItem(mBookId);
                    mTVSubBook.setBackground(getResources().getDrawable(R.drawable.button_bule_bg));
                    Drawable drawable = getResources().getDrawable(R.mipmap.book_detail_info_add_img);
                    drawable.setBounds(0,0,15,15);
                    mTVSubBook.setText("追书");
                    mTVSubBook.setCompoundDrawables(drawable,null,null,null);

                }else {
                    mTVSubBook.setTag(true);
                    CollectionUtil.addCollectionItem(mBook);
                    mTVSubBook.setBackground(getResources().getDrawable(R.drawable.button_gray_bg));
                    Drawable drawable = getResources().getDrawable(R.mipmap.book_detail_info_del_img);
                    drawable.setBounds(0,0,15,15);
                    mTVSubBook.setText("不追了");
                    mTVSubBook.setCompoundDrawables(drawable,null,null,null);
                }

            }
        });

        mTVLatelyFollower.setText(String.valueOf(bookDetail.getLatelyFollower()));
        mTVRetentionRatio.setText(bookDetail.getRetentionRatio()+"%");
        mTVSerializeWordCount.setText(String.valueOf(bookDetail.getSerializeWordCount()));



        mTVContent.setText(bookDetail.getLongIntro());
        mTVContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTVContent.getMaxLines() == 4){
                    mTVContent.setMaxLines(Integer.MAX_VALUE);
                }else {
                    mTVContent.setMaxLines(4);
                }
            }
        });
        mTVCommTag.setText(bookDetail.getTitle()+"的社区");
        mTVPostNum.setText("共有"+bookDetail.getPostCount()+"个帖子");
        mTags.addAll(bookDetail.getTags());
        Log.d("data4",mTags.toString());
        initTagGroup();
    }

    private void initTagGroup() {
        int[] colors = new int[]{
                R.color.light_blue,
                R.color.light_coffee,
                R.color.light_pink,
                R.color.light_red,
                R.color.light_gray,
                R.color.orange,
                R.color.dark_red,
                R.color.green
        };
        Random random = new Random(47);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = (int) SystemUtils.dp2px(mContext,5);
        lp.leftMargin = margin;
        lp.topMargin = margin;
        lp.rightMargin = margin;
        lp.bottomMargin = margin;
        for(int i=0;i<mTags.size();i++){
            TextView view = new TextView(this);
            int padding = margin;
            view.setPadding(padding,0,padding,0);
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //搜索
                }
            });
            view.setText(mTags.get(i));
            view.setTextColor(Color.WHITE);
            view.setBackgroundColor(getResources().getColor(colors[random.nextInt(colors.length)]));
            mTagGroup.addView(view,lp);

        }


    }

    @Override
    public void setHotReview(HotReviewBean hotReview) {
        mHotReviewList.addAll(hotReview.getReviews());
        mHotReviewAdapter.notifyDataSetChanged();

    }

    @Override
    public void setRecommendBookList(RecommendBookList recommendBookList) {
        mRecommendBookList.addAll(recommendBookList.getBooklists());
        mRecommendBookAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_bookdetail_readbutton)
    public void readBook(){
        ReadActivity.jumpToMe(mContext,mBookId,mTVTitle.getText().toString());

    }
    @OnClick(R.id.ll_community)
    public void gotoCommunity(){
        BookDetailCommunityActivity.jumpToMe(mContext,mBookId,mTVTitle.getText().toString(),0);

    }

    @OnClick(R.id.tv_bookdetail_morereview)
    public void gotoReview(){
        BookDetailCommunityActivity.jumpToMe(mContext,mBookId,mTVTitle.getText().toString(),1);
    }

}
