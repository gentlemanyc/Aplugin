package cc.aplugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import cc.plugin.base.PluginManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PluginManager.installPluginFromAssets(this, "Plugin", "Plugin.apk") {
            if (it) {
                PluginManager.startActivity(
                    this,
                    "Plugin",
                    "cc.test.MainActivity",
                    bundleOf(Pair("host", "hehe"))
                )
            }
        }

    }
}