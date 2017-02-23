package com.example.jon.fangreader.di.component;

import android.support.v4.app.Fragment;

import com.example.jon.fangreader.di.module.FragmentModule;
import com.example.jon.fangreader.di.scope.PerFragment;
import com.example.jon.fangreader.ui.fragment.BookShelfFragment;
import com.example.jon.fangreader.ui.fragment.DiscussFragment;
import com.example.jon.fangreader.ui.fragment.ReviewFragment;

import dagger.Component;

/**
 * Created by jon on 2017/1/4.
 */
@PerFragment
@Component(modules = FragmentModule.class,dependencies = AppComponent.class)
public interface FragmentComponent {
    Fragment getFragment();
    void inject(BookShelfFragment bookShelfFragment);
    void inject(DiscussFragment discussFragment);
    void inject(ReviewFragment reviewFragment);
}
