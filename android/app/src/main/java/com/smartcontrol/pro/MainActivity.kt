package com.smartcontrol.pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.smartcontrol.pro.ui.SmartControlRoot
import com.smartcontrol.pro.ui.profile.AppRole

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SmartControlRoot(currentRole = AppRole.OWNER) }
    }
}
