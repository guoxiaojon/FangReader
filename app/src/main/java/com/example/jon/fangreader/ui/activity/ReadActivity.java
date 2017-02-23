package com.example.jon.fangreader.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.app.App;
import com.example.jon.fangreader.base.BaseActivity;
import com.example.jon.fangreader.di.component.DaggerActivityComponent;
import com.example.jon.fangreader.di.module.ActivityModule;
import com.example.jon.fangreader.model.bean.BookMark;
import com.example.jon.fangreader.model.bean.BookTocBean;
import com.example.jon.fangreader.model.bean.DownloadEvent;
import com.example.jon.fangreader.presenter.ReadPresenter;
import com.example.jon.fangreader.presenter.contract.ReadContract;
import com.example.jon.fangreader.service.DownloadService;
import com.example.jon.fangreader.ui.adapter.BookMarkAdapter;
import com.example.jon.fangreader.ui.adapter.PopListAdapter;
import com.example.jon.fangreader.utils.ReaderUtil;
import com.example.jon.fangreader.utils.ScreenUtil;
import com.example.jon.fangreader.utils.SharePreferenceUtil;
import com.example.jon.fangreader.utils.SystemUtils;
import com.example.jon.fangreader.widget.CoolFlipReadView;
import com.example.jon.fangreader.widget.ReadView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * Created by jon on 2017/1/5.
 */

public class ReadActivity extends BaseActivity<ReadPresenter> implements ReadContract.View,SeekBar.OnSeekBarChangeListener,CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.fl_readview_container)
    FrameLayout mFLContainer;
    @BindView(R.id.ll_read_toplist)
    LinearLayout mLLTopList;
    @BindView(R.id.ll_read_bottomlist)
    LinearLayout mLLBottomList;
    @BindView(R.id.iv_read_toplist_back)
    ImageView mIVBack;
    @BindView(R.id.tv_read_toplist_reading)
    TextView mTVReading;
    @BindView(R.id.tv_read_toplist_community)
    TextView mTVCommunity;
    @BindView(R.id.tv_read_toplist_profile)
    TextView mTVProfile;
    @BindView(R.id.tv_read_bottomlist_mode)
    TextView mTVMode;
    @BindView(R.id.tv_read_bottomlist_setting)
    TextView mTVSetting;
    @BindView(R.id.tv_read_bottomlist_cache)
    TextView mTVCache;
    @BindView(R.id.tv_read_bottomlist_bookmark)
    TextView mTVBookmark;
    @BindView(R.id.tv_read_bottomlist_contents)
    TextView mTVContent;
    @BindView(R.id.sb_read_bottomlist_bright)
    SeekBar mSBBirght;
    @BindView(R.id.sb_read_bottomlist_textsize)
    SeekBar mSBTextSize;
    @BindView(R.id.cb_read_bottomlist_autobirght)
    CheckBox mCBAutoBirght;
    @BindView(R.id.cb_read_bottomlist_usevolume)
    CheckBox mCBUseVolume;
    @BindView(R.id.rv_read_bottomlist_bgselector)
    RecyclerView mRVBgSelector;
    @BindView(R.id.ll_read_bottomlist_settinglist)
    LinearLayout mLLSettingList;
    @BindView(R.id.ll_read_bottomlist_bookmarklist)
    LinearLayout mLLBookMarkList;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_read_cacheprogress)
    TextView mTVCacheProgress;
    @BindView(R.id.tv_read_bottomlist_bookmark_add)
    TextView mTVBookmarkAdd;
    @BindView(R.id.tv_read_bottomlist_bookmark_clear)
    TextView mTVBookmarkClear;
    @BindView(R.id.rv_read_bottomlist_bookmark_list)
    RecyclerView mRVBookmarkList;
    ListPopupWindow mContentPopList;
    PopListAdapter mPopListAdapter;


    String mTitle;
    String mBookId;
    ReadView mReadView;
    List<BookTocBean.MixToc.Chapter> mChapters;

    List<BookMark> mBookMarks;
    BookMarkAdapter mBookMarkAdapter;

    public static void jumpToMe(Context context,String bookId, String title){
        Intent intent = new Intent(context,ReadActivity.class);
        intent.putExtra("bookId",bookId);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }

    @Override
    protected void initDataAndEvent() {
        Intent intent = getIntent();
        mBookId = intent.getStringExtra("bookId");
        mTitle = intent.getStringExtra("title");
        mBookMarks = new ArrayList<>();
        mBookMarkAdapter = new BookMarkAdapter(mContext, mBookMarks, new BookMarkAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                BookMark mark = mBookMarks.get(position);
                if(mReadView.openBook(mark.getmChapterIndex(),mark.getmBeginIndex(),mark.getmEndIndex())){
                    mReadView.pageCurr();
                    mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
                    mReadView.invalidate();
                }else {
                    int chapterIndex = mark.getmChapterIndex();
                    mPresenter.getChapterForBookMark(mark,mBookId,mChapters.get(chapterIndex).getLink(),mChapters.get(chapterIndex).getTitle());
                }

            }
        });
        mRVBookmarkList.setAdapter(mBookMarkAdapter);
        mRVBookmarkList.setLayoutManager(new LinearLayoutManager(mContext));


        hideView();

        mChapters = new ArrayList<>();
        mReadView = new CoolFlipReadView(this, mBookId, new ReadView.OnReadStateChangedListener() {
            @Override
            public void onLoadChapter(int chapterIndex,boolean loadNext) {
                mProgressBar.setVisibility(View.VISIBLE);

                mPresenter.getChapter(chapterIndex,mBookId,mChapters.get(chapterIndex).getLink(),loadNext,mChapters.get(chapterIndex).getTitle(),false);

            }

            @Override
            public void onLoadChapterOnStart(int chapterIndex) {

                if(mChapters.size()>0){
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPresenter.getChapterOnStart(chapterIndex,mBookId,mChapters.get(chapterIndex).getLink(),mChapters.get(chapterIndex).getTitle());

                }
            }

            @Override
            public void onClickCenter() {
                Log.d("data","点击中间");
                //Toast.makeText(mContext,"点击中间",Toast.LENGTH_SHORT).show();
//                mLLSettingList.setVisibility(GONE);
                if(mLLTopList.getVisibility() == View.VISIBLE){
//                    mLLTopList.setVisibility(GONE);
//                    mLLBottomList.setVisibility(GONE);
                    hideView();

                }else {
                    showView();
                }
            }

            @Override
            public void onFlip() {

               hideView();
            }

            @Override
            public void onChapterChanged(int chapterIndex) {
               // getChapterSuccessful(chapterIndex);
            }

            @Override
            public void onPageChanged() {

            }

            @Override
            public void onLoadPreChapter() {

            }

            @Override
            public void onLoadNextChapter() {

            }
        });
        mReadView.setLayoutParams(new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mFLContainer.addView(mReadView);
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.getChapterList(mBookId);
        initPopList();


    }

    private void initPopList() {
        mContentPopList = new ListPopupWindow(mContext);
        mContentPopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("data3","poplIstItemOnClick : "+ position);
                if(mReadView.openBook(position)){
                    Log.d("data3","poplIstItemOnClick ==== > openSuccessful");
                    mReadView.pageCurr();
                    mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
                    mReadView.invalidate();
                }else {
                    Log.d("data3","poplIstItemOnClick ==== > downFromNet");
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPresenter.getChapterForBookMark(new BookMark(position,0,0,""),mBookId,mChapters.get(position).getLink(),mChapters.get(position).getTitle());
                }
                mPopListAdapter.setCurrChapter(position);
                mPopListAdapter.notifyDataSetChanged();
                mContentPopList.dismiss();
            }
        });
        mPopListAdapter = new PopListAdapter(mContext, mBookId, 0, mChapters);
        mContentPopList.setAdapter(mPopListAdapter);
        mContentPopList.setAnchorView(mLLTopList);
        mContentPopList.setHeight(ListPopupWindow.WRAP_CONTENT);
        mContentPopList.setWidth(ListPopupWindow.MATCH_PARENT);
        mContentPopList.dismiss();

    }

    private void hideView() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mLLTopList.setVisibility(GONE);
        mLLBottomList.setVisibility(GONE);
        mLLSettingList.setVisibility(GONE);
        mLLBookMarkList.setVisibility(GONE);
        mTVCacheProgress.setVisibility(GONE);

    }

    private void showView() {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mLLTopList.setVisibility(View.VISIBLE);
        mLLBottomList.setVisibility(View.VISIBLE);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read;
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
    public void setChapterList(List<BookTocBean.MixToc.Chapter> chapterList) {
        mProgressBar.setVisibility(GONE);
        mChapters.clear();
        mChapters.addAll(chapterList);
        mReadView.setChapters(mChapters);
        initView();
        mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
        mReadView.invalidate();

    }

    @Override
    public void getChapterSuccessfulOnStart(int chapterIndex) {
        mProgressBar.setVisibility(GONE);
        mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
        mReadView.invalidate();

    }

    @Override
    public void getChapterSuccessful(int chapterIndex,boolean loadNext,boolean useVolume) {
        mProgressBar.setVisibility(GONE);
        if(loadNext){
            if(useVolume){
                mReadView.openBook(chapterIndex);
                mReadView.pageNext();
                mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
                mReadView.invalidate();
            }else {
                mReadView.refreshNextCanvasForNext(chapterIndex);
                mReadView.invalidate();
            }

        }else {
            if(useVolume){
                mReadView.openBook(chapterIndex);
                mReadView.calcIndexOfLastPage();
                mReadView.pageCurr();
                mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
                mReadView.invalidate();

            }else {
                mReadView.refreshNextCanvasForPre(chapterIndex);
                mReadView.invalidate();
            }

        }

    }

    @Override
    public void setCacheProgress(DownloadEvent event) {
        if(mLLBottomList.getVisibility() == View.VISIBLE){
            mTVCacheProgress.setVisibility(View.VISIBLE);
        }else {
            mTVCacheProgress.setVisibility(GONE);
        }

        if(event.isFinished()){
            mTVCacheProgress.setText("完成");
            mTVCacheProgress.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTVCacheProgress.setVisibility(GONE);
                }
            },100);
        }else {
            mTVCacheProgress.setText(event.getTitle()+"("+event.getCurrentChapter()+"/"+event.getTotalChapter()+")···");
        }


    }

    @Override
    public void setBookMark(List<BookMark> marks) {
        Log.d("data","setBookMark()");
        mBookMarks.clear();
        mBookMarks.addAll(marks);
        mBookMarkAdapter.notifyDataSetChanged();
    }

    @Override
    public void getChapterForBookMarkSuccessful(BookMark bookMark) {
        mProgressBar.setVisibility(GONE);
        mReadView.openBook(bookMark.getmChapterIndex(),bookMark.getmBeginIndex(),bookMark.getmEndIndex());
        mReadView.pageCurr();
        mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
        mReadView.invalidate();
    }

    private void initView(){
        Drawable drawable = null;
        if(SharePreferenceUtil.isNight()){
            mTVMode.setText("日间");
            drawable = ContextCompat.getDrawable(this,R.mipmap.ic_menu_mode_day_manual);
            mReadView.changeMode(ReaderUtil.NIGHT_BAKCGROUND);
            mReadView.setTextColor(R.color.chapter_content_night);
        }else {
            mTVMode.setText("夜间");
            drawable = ContextCompat.getDrawable(this,R.mipmap.ic_menu_mode_night_normal);
            mReadView.changeMode(SharePreferenceUtil.getMode());
            mReadView.setTextColor(R.color.chapter_content_day);
        }


        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTVMode.setCompoundDrawables(null, drawable, null, null);

        mSBBirght.setOnSeekBarChangeListener(this);
        mSBTextSize.setOnSeekBarChangeListener(this);

        mSBTextSize.setProgress(SharePreferenceUtil.getTextSize() * 3);

        mSBBirght.setProgress(SharePreferenceUtil.getBirght());
        mCBAutoBirght.setChecked(SharePreferenceUtil.isAutoBirght());
        mCBUseVolume.setChecked(SharePreferenceUtil.isUseVolume());
        mCBUseVolume.setOnCheckedChangeListener(this);
        mCBAutoBirght.setOnCheckedChangeListener(this);

        if(SharePreferenceUtil.isAutoBirght()){
            ScreenUtil.startAutoBrightness(this);
        }else {
            ScreenUtil.stopAutoBrightness(this);
        }
        mReadView.setFontSize(SystemUtils.sp2px(mContext,SharePreferenceUtil.getTextSize()));
        Log.d("data2","TextSize : " + SharePreferenceUtil.getTextSize());




    }
    @OnClick(R.id.tv_read_bottomlist_mode)
    public void changeMode(){
        Drawable drawable = null;
        if(SharePreferenceUtil.isNight()){
            SharePreferenceUtil.setNight(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            mTVMode.setText("夜间");
            drawable = ContextCompat.getDrawable(this,R.mipmap.ic_menu_mode_night_normal);
            mReadView.changeMode(SharePreferenceUtil.getMode());
            mReadView.setTextColor(R.color.chapter_content_day);
        }else {
            SharePreferenceUtil.setNight(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            mTVMode.setText("日间");
            drawable = ContextCompat.getDrawable(this,R.mipmap.ic_menu_mode_day_manual);
            mReadView.changeMode(ReaderUtil.NIGHT_BAKCGROUND);
            mReadView.setTextColor(R.color.chapter_content_night);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTVMode.setCompoundDrawables(null, drawable, null, null);
        mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
        mReadView.postInvalidate();


    }
    @OnClick(R.id.tv_read_bottomlist_setting)
    public void setting(){
        if(mLLSettingList.getVisibility() == View.VISIBLE){
            mLLSettingList.setVisibility(GONE);
        }else {
            mLLSettingList.setVisibility(View.VISIBLE);
        }


    }
    @OnClick(R.id.tv_read_bottomlist_cache)
    public void cache(){
        mLLTopList.setVisibility(GONE);
        mLLBottomList.setVisibility(GONE);
        mLLSettingList.setVisibility(GONE);
        mLLBookMarkList.setVisibility(GONE);
        new AlertDialog.Builder(this)
                .setTitle("缓存多少")
                .setItems(new String[]{"后五十章", "后面全部", "全部"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int end =0 ,begin = 0;
                        switch (which){
                            case 0:
                                begin = mReadView.getCurrChapterIndex()+1;
                                end = begin + 49;
                                if(end >= mChapters.size()){
                                    end = mChapters.size()-1;
                                }
                                break;
                            case 1:
                                begin = mReadView.getCurrChapterIndex()+1;
                                end = mChapters.size()-1;
                                break;
                            case 2:
                                begin = 0;
                                end = mChapters.size()-1;
                                break;
                            default:
                                break;
                        }
                        for(int i=begin;i<=end;i++){
                            Intent intent = new Intent(mContext, DownloadService.class);
                            intent.putExtra("url",mChapters.get(i).getLink());
                            intent.putExtra("bookId",mBookId);
                            intent.putExtra("currChapter",i-begin+1);
                            intent.putExtra("count",end-begin+1);
                            intent.putExtra("title",mChapters.get(i).getTitle());
                            mContext.startService(intent);
                            Log.d("data3","->"+(i-begin+1)+"/"+(end-begin+1));


                        }
                        showView();
                        dialogInterface.dismiss();

                    }
                }).show();



    }

    @OnClick(R.id.tv_read_bottomlist_bookmark)
    public void bookMark(){
        if(mLLBookMarkList.getVisibility() == View.VISIBLE){
            mLLBookMarkList.setVisibility(GONE);
        }else {
            mPresenter.getBookMark(mBookId);
            mLLBookMarkList.setVisibility(View.VISIBLE);
        }

    }
    @OnClick(R.id.tv_read_toplist_community)
    public void openCommunity(){

        BookDetailCommunityActivity.jumpToMe(mContext,mBookId,mTitle,0);
    }
    @OnClick(R.id.tv_read_toplist_profile)
    public void gotoDetail(){
        BookDetailActivity.jumpToMe(mContext,mBookId);
    }

    @OnClick(R.id.tv_read_bottomlist_contents)
    public void openContent(){
        if(mContentPopList.isShowing()){
            mContentPopList.dismiss();
        }else {
            mPopListAdapter.setCurrChapter(mReadView.getCurrChapterIndex());
            mContentPopList.show();
        }


    }
    @OnClick(R.id.tv_read_bottomlist_bookmark_add)
    public void addBookMark(){
        int[] index = mReadView.getIndex();
        Log.d("data3",mBookMarks.toString());
        BookMark mark = new BookMark(mReadView.getCurrChapterIndex(),index[0],index[1],mReadView.getDescriptionOfThisPage());
        if(!mBookMarks.contains(mark)){
            mBookMarks.add(mark);
            mBookMarkAdapter.notifyDataSetChanged();
            mPresenter.saveBookMark(mBookMarks,mBookId);
        }


    }
    @OnClick(R.id.tv_read_bottomlist_bookmark_clear)
    public void clearBookMark(){
        if(mBookMarks != null){
            mBookMarks.clear();
            mBookMarkAdapter.notifyDataSetChanged();
            mPresenter.saveBookMark(mBookMarks,mBookId);
        }

    }

    @OnClick(R.id.iv_read_toplist_back)
    public void back(){
        if(mLLTopList.getVisibility() == View.VISIBLE){
            if(mContentPopList.isShowing()){
                mContentPopList.dismiss();
            }else {
                //hideView();
                finish();
            }

        }else {
            finish();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(seekBar.getId() == R.id.sb_read_bottomlist_bright){
            if(!SharePreferenceUtil.isAutoBirght()){
                SharePreferenceUtil.setBirght(i);
                ScreenUtil.setScreenBrightness(i,this);
            }
        }else {
            Log.d("data2","progre : "+i);
            mReadView.setFontSize(SystemUtils.sp2px(mContext,i/3));
            SharePreferenceUtil.setTextSize(i/3);
            Log.d("data2","========");
            SharePreferenceUtil.getTextSize();
            Log.d("data2","--------");
            mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.getId() == R.id.cb_read_bottomlist_autobirght){
            mCBAutoBirght.setChecked(b);
            SharePreferenceUtil.setAutoBright(b);
            if(b){
                ScreenUtil.startAutoBrightness(this);
            }else {
                ScreenUtil.stopAutoBrightness(this);
            }

        }else {
            mCBUseVolume.setChecked(b);
            SharePreferenceUtil.setUseVolume(b);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mLLBottomList.getVisibility() == View.VISIBLE){
                if(mContentPopList.isShowing()){
                    mContentPopList.dismiss();
                }else {
                    hideView();
                }

                mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
                return true;
            }
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            if(SharePreferenceUtil.isUseVolume()){
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(SharePreferenceUtil.isUseVolume()){
            if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
                if(mReadView.hasPrevious()){
                    if(mReadView.pagePrevious()){
                        mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
                        mReadView.invalidate();
                    }else {
                        if(mReadView.openBook(mReadView.getCurrChapterIndex())){
                            mReadView.calcIndexOfLastPage();
                            mReadView.pageCurr();
                            mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
                            mReadView.invalidate();
                        }else {
                            int chapterIndex = mReadView.getCurrChapterIndex();
                            mProgressBar.setVisibility(View.VISIBLE);
                            mPresenter.getChapter(chapterIndex,
                                    mBookId,mChapters.get(chapterIndex).getLink(),false,mChapters.get(chapterIndex).getTitle() ,true);
                        }
                    }

                }else {
                    Toast.makeText(mContext,"没有上一页了~",Toast.LENGTH_SHORT).show();
                }

                return true;
            } else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
                if(mReadView.hasNext()){
                    if(mReadView.pageNext()){
                        Log.d("data2","volume pageNext");
                        mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
                        mReadView.invalidate();
                    }else {
                        if(mReadView.openBook(mReadView.getCurrChapterIndex())){
                            mReadView.pageNext();
                            mReadView.refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
                            mReadView.invalidate();
                        }else {

                            int chapterIndex = mReadView.getCurrChapterIndex();
                            mProgressBar.setVisibility(View.VISIBLE);
                            mPresenter.getChapter(chapterIndex,
                                    mBookId, mChapters.get(chapterIndex).getLink(), true, mChapters.get(chapterIndex).getTitle(),true);

                        }
                    }

                }else {
                    Toast.makeText(mContext,"没有下一页了~",Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        }

        return super.onKeyUp(keyCode, event);
    }
}
