package com.merseyside.merseyLib.oauth.android.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import com.merseyside.archy.presentation.activity.BaseActivity
import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.merseyLib.kotlin.serialization.deserialize
import com.merseyside.merseyLib.kotlin.serialization.serialize
import com.merseyside.merseyLib.oauth.android.OAuthWebViewClient
import com.merseyside.merseyLib.oauth.android.R
import com.merseyside.merseyLib.oauth.core.OAuthConfig
import com.merseyside.merseyLib.oauth.core.OAuthResult
import com.merseyside.utils.ext.getSerialize

class OAuthActivity : BaseActivity() {

    private val client by lazy { OAuthWebViewClient(getOAuthConfig()) }
    private lateinit var webView: WebView

    private val config: OAuthConfig by lazy { getOAuthConfig() }

    override fun getLayoutId() = R.layout.activity_browser
    override fun getToolbar() = null
    override fun getFragmentContainer() = null

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (Build.VERSION.SDK_INT in 21..24) {
            overrideConfiguration.uiMode =
                overrideConfiguration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = findViewById(R.id.web_view)
        webView.webViewClient = client

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            setSupportZoom(true)
            defaultTextEncodingName = "utf-8"
        }

        client.setOnAccessTokenCallback(object : OAuthWebViewClient.OnAccessTokenCallback {
            override fun onResponse(uri: Uri) {
                Logger.log(this@OAuthActivity, uri)
                val intent = Intent().apply {
                    putExtra(RESULT_KEY, uriToOAuthResult(uri).serialize())
                }

                setResult(Activity.RESULT_OK, intent)
                finish()
            }

        })


        val uri = config.oauthUri
        webView.loadUrl(uri).also { Logger.log(this, uri) }
    }

    override fun onStop() {
        super.onStop()

        webView.clearCache(true)
        webView.clearHistory()
        clearCookie()
    }

    override fun performInjection(bundle: Bundle?, vararg params: Any) {}

    private fun clearCookie() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        }
    }

    private fun uriToOAuthResult(uri: Uri?): OAuthResult {
        return uri?.let {
            val query = uri.query
            val paramPairs = uri.queryParameterNames.map {
                it to (uri.getQueryParameter(it)
                ?: throw IllegalArgumentException())
            }
            OAuthResult.Success(query, paramPairs)
        } ?: OAuthResult.Fail
    }


    private fun getOAuthConfig(): OAuthConfig {
        intent.extras?.let { extras ->
            if (extras.containsKey(CONFIG_KEY)) {
                return extras.getSerialize(CONFIG_KEY) ?: throw IllegalArgumentException()
            }
        }

        throw IllegalArgumentException()
    }

    companion object {
        private const val CONFIG_KEY = "config"

        const val RESULT_KEY = "result"
        const val ERROR_KEY = "error"

        fun getStartAuthFlowIntent(context: Context, oAuthConfig: OAuthConfig) =
            Intent(context, OAuthActivity::class.java).apply {
                putExtra(CONFIG_KEY, oAuthConfig.serialize())
            }

        fun parseAuthResult(resultCode: Int, intent: Intent?): OAuthResult {
            return if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(RESULT_KEY)?.deserialize() ?: OAuthResult.Fail
            } else OAuthResult.Fail
        }
    }
}