package com.example.jon.fangreader.ui.activity;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.app.App;
import com.example.jon.fangreader.base.BaseActivity;
import com.example.jon.fangreader.component.RxBus;
import com.example.jon.fangreader.di.component.DaggerActivityComponent;
import com.example.jon.fangreader.di.module.ActivityModule;
import com.example.jon.fangreader.model.bean.SyncBookShelfEvent;
import com.example.jon.fangreader.presenter.MainPresenter;
import com.example.jon.fangreader.presenter.contract.MainContract;
import com.example.jon.fangreader.ui.adapter.MainAdapter;
import com.example.jon.fangreader.ui.fragment.BookShelfFragment;
import com.example.jon.fangreader.ui.fragment.CommunityFragment;
import com.example.jon.fangreader.ui.fragment.FindFragment;
import com.example.jon.fangreader.utils.DateUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    @BindView(R.id.tb_main)
    TabLayout mTabLayout;
    @BindView(R.id.vp_main)
    ViewPager mViewPager;

    private List<Fragment> mFragments;
    private BookShelfFragment mBookShelfFragment;
    private CommunityFragment mCommunityFragment;
    private FindFragment mFindFragment;
    private String[] mTabs;
    private MainAdapter mAdapter;




    @Override
    protected void initDataAndEvent() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ActivityCompat.getColor(mContext,R.color.colorSelected));
        mTabs = new String[]{"书架","社区","发现"};
        mFragments = new ArrayList<Fragment>();
        mFragments.add(mBookShelfFragment = new BookShelfFragment());
        mFragments.add(mCommunityFragment = new CommunityFragment());
        mFragments.add(mFindFragment = new FindFragment());
        mAdapter = new MainAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mAdapter);

        for(int i = 0; i< mTabs.length; i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(mTabs[i]));
        }

        mTabLayout.setupWithViewPager(mViewPager);

        for(int i=0;i<mTabs.length;i++){
            mTabLayout.getTabAt(i).setText(mTabs[i]);
        }
        mPresenter.syncBookShelf();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mPresenter.syncBookShelf();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void syncBookShelfCompleted() {
        RxBus.getDefault().post(new SyncBookShelfEvent());

    }

    private double mCurrTime;

    @Override
    public void onBackPressed() {
        if(mCurrTime != 0 && DateUtil.getCurrentMillioneSceond()- mCurrTime >2000){
            super.onBackPressed();
        }else {
            mCurrTime = DateUtil.getCurrentMillioneSceond();
            Snackbar.make(getWindow().getDecorView(),"再按一次退出",Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCurrTime = 0;
    }
}
