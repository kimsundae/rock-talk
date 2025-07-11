package com.zikkeunzikkeun.rocktalk;

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.zikkeunzikkeun.rocktalk.ui.theme.Strings

class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        NaverIdLoginSDK.initialize(this, Strings.Key.NAVER_WEB_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECREAT_KEY, Strings.Text.ROCK_TALK)
//        Log.i(null,Utility.getKeyHash(this))
    }
}
