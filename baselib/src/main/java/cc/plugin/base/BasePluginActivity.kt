package cc.plugin.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import java.lang.RuntimeException

private const val TAG = "BasePluginActivity"

open class BasePluginActivity : AppCompatActivity() {
    var hostContext: AppCompatActivity? = null
    var pluginRes: Resources? = null
    lateinit var pluginInfo: PluginInfo
    override fun getResources(): Resources {
        if (pluginRes != null) {
            return pluginRes!!
        }
        return super.getResources()
    }

    override fun setContentView(layoutResID: Int) {
        if (hostContext == null) {
            super.setContentView(layoutResID)
            return
        }
        LayoutInflater.from(hostContext)
            .inflate(layoutResID, hostContext!!.window.decorView as ViewGroup, true)
    }

    var setOnce = false
    public override fun attachBaseContext(newBase: Context?) {
        if (!setOnce) {
            super.attachBaseContext(newBase)
            setOnce = true
        }
    }


    override fun <T : View?> findViewById(id: Int): T {
        if (hostContext == null) {
            return super.findViewById<T>(id)
        }
        return hostContext!!.findViewById<T>(id)
    }

    override fun getBaseContext(): Context {
        return hostContext ?: super.getBaseContext()
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        if (hostContext == null) {
            super.onCreate(savedInstanceState)
        } else {
            kotlin.runCatching {
                super.onCreate(savedInstanceState)
            }.getOrElse {
                Log.e(TAG, "onCreate: ", it)
            }
        }
    }

    public override fun onRestart() {
        if (hostContext == null) {
            super.onRestart()
        } else {
            kotlin.runCatching { super.onRestart() }
        }
    }

    public override fun onStart() {
        if (hostContext == null) {
            super.onStart()
        } else {
            kotlin.runCatching { super.onStart() }
        }
    }

    public override fun onResume() {
        if (hostContext == null) {
            super.onResume()
        } else {
            kotlin.runCatching { super.onResume() }
        }
    }

    public override fun onPause() {
        if (hostContext == null) {
            super.onPause()
        } else {
            kotlin.runCatching { super.onPause() }

        }
    }

    public override fun onStop() {
        if (hostContext == null) {
            super.onStop()
        } else {
            kotlin.runCatching { super.onStop() }
        }
    }

    public override fun onDestroy() {
        if (hostContext == null) {
            super.onDestroy()
        } else {
            kotlin.runCatching { super.onDestroy() }

        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        if (hostContext == null) {
            super.onSaveInstanceState(outState)
        } else {
            kotlin.runCatching { super.onSaveInstanceState(outState) }
        }
    }

    public override fun onNewIntent(intent: Intent?) {
        if (hostContext == null) {
            super.onNewIntent(intent)
        } else {
            kotlin.runCatching { super.onNewIntent(intent) }
        }
    }

    fun createRes(
        apkPath: String,
        hostAppContext: Context,
    ) {
        val packageManager = hostAppContext.packageManager
        val packageInfo = PackageInfo()
        packageInfo.versionCode = 0
        packageInfo.versionName = "0"
        packageInfo.applicationInfo = hostAppContext.applicationInfo
        packageInfo.applicationInfo.publicSourceDir = apkPath
        packageInfo.applicationInfo.sourceDir = apkPath
        try {
            pluginRes =
                packageManager.getResourcesForApplication(packageInfo.applicationInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException(e)
        }
    }

    override fun getPackageName(): String {
        if (hostContext == null) {
            return super.getPackageName()
        } else {
            return hostContext!!.packageName ?: ""
        }
    }

    override fun getWindow(): Window {
        if (hostContext == null) {
            return super.getWindow()
        } else {
            return hostContext!!.window
        }
    }

    override fun getApplicationContext(): Context {
        if (hostContext == null) {
            return super.getApplicationContext()
        } else {
            return hostContext!!.applicationContext
        }
    }

    override fun getPackageManager(): PackageManager {
        if (hostContext == null) {
            return super.getPackageManager()
        } else {
            return hostContext!!.packageManager
        }
    }

    override fun getApplicationInfo(): ApplicationInfo {
        if (hostContext == null) {
            return super.getApplicationInfo()
        } else {
            return hostContext!!.applicationInfo
        }
    }

    override fun getComponentName(): ComponentName {
        if (hostContext == null) {
            return super.getComponentName()
        } else {
            return hostContext!!.componentName
        }
    }

    override fun getSystemService(name: String): Any? {
        if (hostContext == null) {
            return super.getSystemService(name)
        } else {
            return hostContext!!.getSystemService(name)
        }
    }

    override fun getSystemServiceName(serviceClass: Class<*>): String? {
        if (hostContext == null) {
            return super.getSystemServiceName(serviceClass)
        } else {
            return hostContext!!.getSystemServiceName(serviceClass)
        }
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
    }
}