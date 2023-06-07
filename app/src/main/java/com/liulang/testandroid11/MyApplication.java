package com.liulang.testandroid11;

import android.app.Application;

import kdthe.Bx6E1Helper;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bx6E1Helper.initBx(this);
    }
}
