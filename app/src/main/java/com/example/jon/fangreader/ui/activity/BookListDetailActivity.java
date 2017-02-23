package com.example.jon.fangreader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.app.App;
import com.example.jon.fangreader.base.BaseActivity;
import com.example.jon.fangreader.component.ImageLoader;
import com.example.jon.fangreader.di.component.DaggerActivityComponent;
import com.example.jon.fangreader.di.module.ActivityModule;
import com.example.jon.fangreader.model.bean.BookListDetailBean;
import com.example.jon.fangreader.model.bean.RecommendBookList;
import com.example.jon.fangreader.presenter.BookListDetailPresenter;
import com.example.jon.fangreader.presenter.contract.BookListDetailContract;
import com.example.jon.fangreader.ui.adapter.BookListDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jon on 2017/2/23.
 */

public class BookListDetailActivity extends BaseActivity<BookListDetailPresenter> implements BookListDetailContract.View {
    public static final String BOOK_LIST = "book_list";
    private String mBookListId;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_booklistdetail_container)
    RecyclerView mRVBookContainer;

    private HeaderView mHeaderView;

    private BookListDetailAdapter mAdapter;
    private List<BookListDetailBean.BookListBean.BooksBean> mBookList;

    RecommendBookList.RecommendBook mBook;


    public static void jumpToMe(Context context, RecommendBookList.RecommendBook book){
        Intent intent = new Intent(context,BookListDetailActivity.class);
        intent.putExtra(BOOK_LIST,book);
        context.startActivity(intent);
    }
    @Override
    protected void initDataAndEvent() {
        mBook = (RecommendBookList.RecommendBook) getIntent().getSerializableExtra(BOOK_LIST);
        mBookListId = mBook.getId();
        initToolBar();
        mBookList = new ArrayList<>();
        mHeaderView = new HeaderView(LayoutInflater.from(mContext).inflate(R.layout.activity_booklistdetail_header,null,false));
        mAdapter = new BookListDetailAdapter(mBookList, mHeaderView, mContext, new BookListDetailAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                BookDetailActivity.jumpToMe(mContext,mBookList.get(position).getBook().get_id());
            }
        });
        mRVBookContainer.setAdapter(mAdapter);
        mRVBookContainer.setLayoutManager(new LinearLayoutManager(mContext));
        mPresenter.getBookListDetail(mBookListId);

    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("书单详情");
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_booklistdetail;
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
    public void setBookListDetail(BookListDetailBean bean) {
        mBookList.addAll(bean.getBookList().getBooks());
        mHeaderView.tvTitle.setText(bean.getBookList().getTitle());
        mHeaderView.tvDesc.setText(bean.getBookList().getDesc());
        mHeaderView.tvName.setText(bean.getBookList().getAuthor().getNickname());
        ImageLoader.load(mContext,bean.getBookList().getAuthor().getAvatar(),mHeaderView.ivPic);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_booklistdetail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_collec){

            mPresenter.collecBookList(mBook);
        }
        return super.onOptionsItemSelected(item);
    }
    class HeaderView extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_booklistdetail_title)
        TextView tvTitle;
        @BindView(R.id.tv_booklistdetail_desc)
        TextView tvDesc;
        @BindView(R.id.iv_booklistdetail_pic)
        ImageView ivPic;
        @BindView(R.id.tv_booklistdetail_name)
        TextView tvName;

        public HeaderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
