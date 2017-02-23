package com.example.jon.fangreader.presenter.contract;

import com.example.jon.fangreader.base.BasePresenter;
import com.example.jon.fangreader.base.BaseView;

/**
 * Created by jon on 2017/1/2.
 */

public interface MainContract {
    interface View extends BaseView{
        void syncBookShelfCompleted();

    }
    interface Presenter extends BasePresenter<View>{
        void syncBookShelf();
    }
}
