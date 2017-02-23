package com.example.jon.fangreader.di.module;

import android.support.v4.app.Fragment;

import com.example.jon.fangreader.di.scope.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jon on 2017/1/3.
 */
@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment){
        this.mFragment = fragment;
    }

    @PerFragment
    @Provides
    Fragment provideFragment(){
        return mFragment;
    }

}

