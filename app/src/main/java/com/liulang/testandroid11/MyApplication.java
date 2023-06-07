package com.liulang.testandroid11;

import android.app.Application;

import kdthe.Bx6E1Helper;

/**
 * @author liulang
 * @date 2023-06-07 15:22
 * @company 湖南科大天河通信股份有限公司
 * @description
 **/

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bx6E1Helper.initBx(this);
    }
}
