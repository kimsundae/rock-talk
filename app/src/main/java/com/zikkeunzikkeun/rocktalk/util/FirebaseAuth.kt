package com.zikkeunzikkeun.rocktalk.util

import com.google.firebase.auth.FirebaseAuth

fun getUserId(): String? {
    return FirebaseAuth.getInstance().currentUser?.uid
}

