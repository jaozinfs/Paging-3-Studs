package com.jaozinfs.moovs.utils

import android.net.Uri

object UriUtils {
    fun getUriFromBaseAndBackdrop(base: String, backdrop: String): Uri = Uri.parse(base)
        .buildUpon()
        .appendEncodedPath(backdrop)
        .build()
}