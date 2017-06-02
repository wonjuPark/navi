package com.app.navi.main;

import android.app.Application;

/**
 * Created by 95016056 on 2017-05-31.
 */

public class MyApplication extends Application {
    private static String naviDiv = "TAPI"; /* 1: T_Map, 2: Kakao_Map */
    private static String jusoAPIKey = ""; /* 행자부 API */
    private static String naviAPIKey = ""; /* T_MAP or 2: Kakao_Map */

    @Override
    public void onCreate() {
        //전역 변수 초기화
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public String getNaviDiv() {
        return naviDiv;
    }

    public void setNaviDiv(String naviDiv) {
        MyApplication.naviDiv = naviDiv;
    }

    public String getJusoAPIKey() {
        return jusoAPIKey;
    }

    public void setJusoAPIKey(String jusoAPIKey) {
        MyApplication.jusoAPIKey = jusoAPIKey;
    }

    public String getNaviAPIKey() {
        return naviAPIKey;
    }

    public void setNaviAPIKey(String naviAPIKey) {
        MyApplication.naviAPIKey = naviAPIKey;
    }

}
