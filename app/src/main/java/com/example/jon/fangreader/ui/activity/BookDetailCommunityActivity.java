package com.example.jon.fangreader.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.base.SimpleActivity;
import com.example.jon.fangreader.component.RxBus;
import com.example.jon.fangreader.model.bean.SortEvent;
import com.example.jon.fangreader.ui.adapter.CommunityAdapter;
import com.example.jon.fangreader.ui.fragment.DiscussFragment;
import com.example.jon.fangreader.ui.fragment.ReviewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.jon.fangreader.app.Constants.SORT_COMMENT_COUNT;
import static com.example.jon.fangreader.app.Constants.SORT_CREATED;
import static com.example.jon.fangreader.app.Constants.SORT_DEFAULT;

/**
 * Created by jon on 2017/2/13.
 */

public class BookDetailCommunityActivity extends SimpleActivity {
    public static final String INTENT_ID = "bookId";
    public static final String INTENT_TITLE = "title";
    public static final String INTENT_INDEX = "index";




    private String mBookId;
    private String mTitle;
    private int mIndex;
    @BindView(R.id.tl_community_tab)
    TabLayout mTabLayout;
    @BindView(R.id.vp_community_container)
    ViewPager mVPContainer;
    Fragment mDiscussFragment;
    Fragment mReviewFragment;

    CommunityAdapter mCommunityAdapter;
    List<Fragment> mFragments;
    String mSort = SORT_DEFAULT;

    @Override
    protected void initDataAndEvent() {
        Intent intent = getIntent();
        mBookId = intent.getStringExtra(INTENT_ID);
        mTitle = intent.getStringExtra(INTENT_TITLE);
        mIndex = intent.getIntExtra(INTENT_INDEX,0);
        initToolBar();
        mDiscussFragment = DiscussFragment.newInstance(mBookId,mSort);
        mReviewFragment = ReviewFragment.newInstance(mBookId,mSort);
        mFragments = new ArrayList<>();
        mFragments.add(mDiscussFragment);
        mFragments.add(mReviewFragment);
        mCommunityAdapter = new CommunityAdapter(getSupportFragmentManager(),mFragments);
        mVPContainer.setAdapter(mCommunityAdapter);
        String[] titles = new String[]{"讨论","书评"};
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[0]));
        mTabLayout.addTab(mTabLayout.newTab().setTag(titles[1]));
        mTabLayout.setupWithViewPager(mVPContainer);
        mTabLayout.getTabAt(0).setText(titles[0]);
        mTabLayout.getTabAt(1).setText(titles[1]);
        mVPContainer.setCurrentItem(mIndex);


    }

    private void initToolBar() {
        setSupportActionBar(mToolBar);
        Log.d("data","~~~~~~~~~+++++++++++==================>>>>>"+mTitle);
        getSupportActionBar().setTitle(mTitle);
        mToolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_community,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_sort){
            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    int mChecked;
    private void showDialog(){
        new AlertDialog.Builder(mContext)
                .setTitle("排序")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setSingleChoiceItems(new String[]{"默认排序", "最新发布", "最多评论"}, mChecked, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        mChecked = which;
                        switch (which){
                            case 0:
                                mSort = SORT_DEFAULT;
                                RxBus.getDefault().post(new SortEvent(SORT_DEFAULT));
                                break;
                            case 1:
                                mSort = SORT_CREATED;
                                RxBus.getDefault().post(new SortEvent(SORT_CREATED));
                                break;
                            case 2:
                                mSort = SORT_COMMENT_COUNT;
                                RxBus.getDefault().post(new SortEvent(SORT_COMMENT_COUNT));
                                break;
                            default:
                                break;
                        }
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();

    }

    @Override
    protected int getLayouId() {
        return R.layout.activity_bookdetailcommunity;
    }
    public static void jumpToMe(Context context, String bookId, String title, int index){
        Intent intent = new Intent(context,BookDetailCommunityActivity.class);
        intent.putExtra(INTENT_ID,bookId);
        intent.putExtra(INTENT_TITLE,title);
        intent.putExtra(INTENT_INDEX,index);
        context.startActivity(intent);

    }
}
