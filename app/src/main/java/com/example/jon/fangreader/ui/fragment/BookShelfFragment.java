package com.example.jon.fangreader.ui.fragment;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.app.App;
import com.example.jon.fangreader.base.BaseFragment;
import com.example.jon.fangreader.di.component.DaggerFragmentComponent;
import com.example.jon.fangreader.di.module.FragmentModule;
import com.example.jon.fangreader.model.bean.DownloadEvent;
import com.example.jon.fangreader.model.bean.RecommendBean;
import com.example.jon.fangreader.presenter.BookShelfPresenter;
import com.example.jon.fangreader.presenter.contract.BookShelfContract;
import com.example.jon.fangreader.ui.activity.ReadActivity;
import com.example.jon.fangreader.ui.adapter.BookShelfAdapter;
import com.example.jon.fangreader.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by jon on 2017/1/4.
 */

public class BookShelfFragment extends BaseFragment<BookShelfPresenter> implements BookShelfContract.View{
    @BindView(R.id.tv_bookshelf_tips)
    TextView mTVTips;
    @BindView(R.id.rv_bookshelf)
    RecyclerView mRVBookShelf;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.ll_bookshelf_bottom)
    LinearLayout mLLBottom;
    @BindView(R.id.tv_bookshelf_select)
    TextView mTVBottomSelect;
    @BindView(R.id.tv_bookshelf_delete)
    TextView mTVBottomDelete;

    private BookShelfAdapter mAdapter;
    private List<RecommendBean.Book> mCollectionList;

    private boolean mIsSelectedAll;



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
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void initDataAndEvent() {
        mIsSelectedAll = false;
        mCollectionList = new ArrayList<>();
        mRVBookShelf.addItemDecoration(new DividerItemDecoration(mActivity,DividerItemDecoration.VERTICAL));
        mAdapter = new BookShelfAdapter(mContext, mCollectionList, new BookShelfAdapter.OnClickListener() {
            @Override
            public void onLongClick(RecommendBean.Book book) {
                //显示对话框
                if(mLLBottom.getVisibility() == View.VISIBLE){
                    return;
                }
                showLongClickDialog(book);
            }

            @Override
            public void onClick(RecommendBean.Book book,int position) {
                if(mLLBottom.getVisibility() == View.VISIBLE){
                    return;
                }
                if(book == null){
                    //ViewPager切换
                }else {
                    //转到ReadActivity
                    Intent intent = new Intent(mActivity, ReadActivity.class);
                    intent.putExtra("bookId",book.getId());
                    intent.putExtra("title",book.getTitle());
                    mActivity.startActivity(intent);
                    book.setReaded(true);
                    mAdapter.notifyItemChanged(position);
                }
            }
        });
        mRVBookShelf.setAdapter(mAdapter);
        mRVBookShelf.setLayoutManager(new LinearLayoutManager(mContext));
        mProgressBar.setVisibility(View.VISIBLE);

        mPresenter.getRecommendList();



    }

    private void showLongClickDialog(final RecommendBean.Book book) {

        String[] itemNames;
        DialogInterface.OnClickListener onClickListener;
        if(book.isFromLocal()){
            itemNames = new String[]{"置顶","删除","批量管理"};
            if(book.isTop()){
                itemNames[0] = "取消置顶";
            }
            onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i){
                        case 0://置顶、取消置顶
                            Log.d("data","置顶");
                            mPresenter.setTop(book,!book.isTop());
                            break;
                        case 1://删除
                            List<RecommendBean.Book> books = new ArrayList<>();
                            books.add(book);
                            showDeleteDialog(books,"移除"+book.getTitle());
                            break;
                        case 2://批量管理
                            for(int j =0;j<mCollectionList.size();j++){
                                mCollectionList.get(j).setSelectState(true);
                            }
                            mLLBottom.setVisibility(View.VISIBLE);

                            break;
                        default:
                            break;
                    }
                    dialogInterface.dismiss();
                }
            };

        }else {
            itemNames = new String[]{"置顶","书籍详情","缓存全本","删除","批量管理"};
            if(book.isTop()){
                itemNames[0] = "取消置顶";
            }

            onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d("data","点击"+i);
                    switch (i){
                        case 0:
                            Log.d("data","置顶");
                            mPresenter.setTop(book,!book.isTop());
                            break;
                        case 1:
                            animateShowTips("正在开发");
                            animateDismissTips();
                            break;
                        case 2:
                            mPresenter.cacheBook(book);
                            break;
                        case 3:
                            List<RecommendBean.Book> books = new ArrayList<>();
                            books.add(book);
                            showDeleteDialog(books,"移除"+book.getTitle());
                            break;
                        case 4:
                            for(int j =0;j<mCollectionList.size();j++){
                                mCollectionList.get(j).setSelectState(true);
                            }
                            mAdapter.notifyDataSetChanged();
                            mLLBottom.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                    dialogInterface.dismiss();
                }
            };
        }

        new AlertDialog.Builder(mActivity)
                .setTitle(book.getTitle())
                .setNegativeButton(null,null)
                .setItems(itemNames,onClickListener)
                .setIcon(R.mipmap.book_pic)
                .create()
                .show();
    }

    @Override
    public void showBookShelf(List<RecommendBean.Book> books) {
        mProgressBar.setVisibility(GONE);
        mLLBottom.setVisibility(GONE);
        if(mCollectionList == null){
            return;
        }
        mCollectionList.clear();
        mCollectionList.addAll(books);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void showCacheProgress(DownloadEvent event) {
        if(event.isFinished()){
            animateShowTips(event.getTitle()+" 下载完成");
            animateDismissTips();

        }else {
            animateShowTips(event.getTitle() +" 正在下载 ("+event.getCurrentChapter()+"/"+event.getTotalChapter()+")······");
        }
    }

    private void showDeleteDialog(final List<RecommendBean.Book> toDelete, String title){
        final boolean[] deleteCahce = {true};
        new AlertDialog.Builder(mActivity)
                .setTitle(title)
                .setMultiChoiceItems(new String[]{"同时删除本地缓存"}, deleteCahce, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        deleteCahce[0] = b;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.deleteBooks(toDelete,deleteCahce[0]);
                    }
                })
                .setNegativeButton("取消",null)
                .create()
                .show();


    }

    @OnClick(R.id.tv_bookshelf_select)
    public void selectAll(){
        mIsSelectedAll = !mIsSelectedAll;
        mTVBottomSelect.setText(mIsSelectedAll?"取消全选":"全选");
        for(RecommendBean.Book book : mCollectionList){
            if(book.isSelectState()){
                book.setSelected(mIsSelectedAll);
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    @OnClick(R.id.tv_bookshelf_delete)
    public void deleteSelected(){
        List<RecommendBean.Book> toDelete = new ArrayList<>();
        for(RecommendBean.Book book: mCollectionList){
            if(book.isSelectState() && book.isSelected()){
                toDelete.add(book);

            }


        }
        showDeleteDialog(toDelete,"移除所有书籍");
        mLLBottom.setVisibility(GONE);
    }


    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(mLLBottom.getVisibility() == View.VISIBLE){
                    mLLBottom.setVisibility(GONE);
                    for(RecommendBean.Book book: mCollectionList){
                        book.setSelectState(false);
                        book.setSelected(false);
                    }
                    mAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void showSetTopSuccessful() {
        animateShowTips("置顶成功");
        animateDismissTips();
    }

    @Override
    public void showDeleteSuccessful() {
        animateShowTips("删除成功");
        animateDismissTips();
    }

    private void animateShowTips(String msg){
        if(mTVTips == null){
            return;
        }
        mTVTips.setText(msg);
        if(mTVTips.getVisibility() == VISIBLE){
            return;
        }



        mTVTips.setVisibility(VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, (int) SystemUtils.dp2px(mContext,30));
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int h = (Integer)valueAnimator.getAnimatedValue();
                Log.d("data","hhh:   "+h);
                mTVTips.getLayoutParams().height = h;

                mTVTips.requestLayout();
            }
        });
        valueAnimator.start();



    }

    private void animateDismissTips(){
        if(mTVTips == null){
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) SystemUtils.dp2px(mContext,30),0);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int h = (Integer)valueAnimator.getAnimatedValue();
                Log.d("data","hhhsss:   "+h);
                mTVTips.getLayoutParams().height = h;
                mTVTips.requestLayout();
                if(h == 0){
                    mTVTips.setVisibility(GONE);
                }

            }
        });
        valueAnimator.setStartDelay(2000);
        valueAnimator.start();


    }

}
