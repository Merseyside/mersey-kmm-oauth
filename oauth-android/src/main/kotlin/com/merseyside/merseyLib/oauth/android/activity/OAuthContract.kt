package com.merseyside.merseyLib.oauth.android.activity

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.merseyside.merseyLib.oauth.core.OAuthConfig
import com.merseyside.merseyLib.oauth.core.OAuthResult

class OAuthContract : ActivityResultContract<OAuthConfig, OAuthResult>() {
    override fun createIntent(context: Context, input: OAuthConfig): Intent {
        return OAuthActivity.getStartAuthFlowIntent(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): OAuthResult {
        return OAuthActivity.parseAuthResult(resultCode, intent)
    }
}