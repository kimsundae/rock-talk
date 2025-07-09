package com.zikkeunzikkeun.rocktalk;

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        Log.i("@@@@@", "@@@@@@@@@@@@@@@@@@@@@@@@@@@")
        Log.i(null,Utility.getKeyHash(this))
    }
}
