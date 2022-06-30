package com.merseyside.merseyLib.oauth.android

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.merseyside.merseyLib.oauth.android.activity.OAuthContract
import com.merseyside.merseyLib.oauth.core.OAuthConfig
import com.merseyside.merseyLib.oauth.core.OAuthResult

class OAuth {

    companion object {
        fun registerForActivityResult(
            activity: ComponentActivity,
            callback: ActivityResultCallback<OAuthResult>
        ): ActivityResultLauncher<OAuthConfig> {
            return activity.registerForActivityResult(OAuthContract(), callback)
        }

        fun registerForActivityResult(
            fragment: Fragment,
            callback: ActivityResultCallback<OAuthResult>
        ): ActivityResultLauncher<OAuthConfig> {
            return fragment.registerForActivityResult(OAuthContract(), callback)
        }
    }
}