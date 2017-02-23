package com.example.jon.fangreader.di.component;

import android.app.Activity;

import com.example.jon.fangreader.di.module.ActivityModule;
import com.example.jon.fangreader.di.scope.PerActivity;
import com.example.jon.fangreader.ui.activity.BookDetailActivity;
import com.example.jon.fangreader.ui.activity.BookDiscussionDetailActivity;
import com.example.jon.fangreader.ui.activity.BookListDetailActivity;
import com.example.jon.fangreader.ui.activity.BookReviewDetailActivity;
import com.example.jon.fangreader.ui.activity.MainActivity;
import com.example.jon.fangreader.ui.activity.ReadActivity;

import dagger.Component;

/**
 * Created by jon on 2017/1/3.
 */
@PerActivity
@Component(modules = ActivityModule.class,dependencies = AppComponent.class)
public interface ActivityComponent {
    Activity getActivity();
    void inject(MainActivity mainActivity);
    void inject(ReadActivity readActivity);
    void inject(BookDiscussionDetailActivity bookDiscussionDetailActivity);
    void inject(BookReviewDetailActivity bookReviewDetailActivity);
    void inject(BookDetailActivity bookDetailActivity);
    void inject(BookListDetailActivity bookListDetailActivity);

}
