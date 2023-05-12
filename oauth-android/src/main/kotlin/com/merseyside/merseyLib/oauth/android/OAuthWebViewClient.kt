package com.merseyside.merseyLib.oauth.android

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.merseyLib.oauth.core.OAuthConfig

class OAuthWebViewClient(private val oAuthConfig: OAuthConfig) : WebViewClient() {

    private var onAccessTokenCallback: OnAccessTokenCallback? = null

    interface OnAccessTokenCallback {
        fun onResponse(uri: Uri)
    }

    fun setOnAccessTokenCallback(callback: OnAccessTokenCallback) {
        this.onAccessTokenCallback = callback
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Logger.log(this, request?.url)

        return request?.url?.let {
            if (it.host != null) {
                if (it.toString().contains(oAuthConfig.redirectHost)) {
                    onAccessTokenCallback?.onResponse(request.url)
                } else {
                    view?.loadUrl(it.toString())
                }
            } else {
                view?.loadUrl(it.toString())
            }

            true
        } ?: false
    }
}