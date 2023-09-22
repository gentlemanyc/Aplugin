package cc.plugin.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cc.plugin.base.PluginManager.getPluginInfo
import cc.plugin.base.PluginManager.loadClass

private const val TAG = "DefStubActivity"

class DefStubActivity : AppCompatActivity() {
    private var mPluginActivity: BasePluginActivity? = null
    private var pluginName: String? = null
    private var baseContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val className = intent.getStringExtra(Constants.KEY_CLASS_NAME)
        val pluginName = intent.getStringExtra(Constants.KEY_PLUGIN_NAME)
        this.pluginName = className
        Log.d(TAG, "onCreate: $className")
        val cls = loadClass(pluginName!!, className)
        if (cls != null && cls.superclass != null && cls.superclass.name == BasePluginActivity::class.java.name) {
            try {
                mPluginActivity = cls.newInstance() as BasePluginActivity
                val info = getPluginInfo(
                    pluginName
                )
                if (info != null) {
                    mPluginActivity?.hostContext = this
                    mPluginActivity?.pluginInfo = info
                    mPluginActivity!!.createRes(info.apkPath, this)
                }
            } catch (e: IllegalAccessException) {
                finish()
                Log.e(TAG, "onCreate: ", e)
            } catch (e: InstantiationException) {
                finish()
                Log.e(TAG, "onCreate: ", e)
            }
            runOnUiThread {
                mPluginActivity?.attachBaseContext(baseContext)
                mPluginActivity!!.onCreate(savedInstanceState)
            }
        } else {
            Toast.makeText(this, "不支持的插件！", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setField(fName: String) {
        kotlin.runCatching {
            val field = Activity::class.java.getDeclaredField(fName)
            field.isAccessible = true
            field.set(mPluginActivity, field.get(this))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mPluginActivity?.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mPluginActivity?.onDetachedFromWindow()
    }

    override fun onRestart() {
        super.onRestart()
        mPluginActivity?.onRestart()
    }

    override fun onStart() {
        super.onStart()
        mPluginActivity?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mPluginActivity?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mPluginActivity?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mPluginActivity?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPluginActivity?.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mPluginActivity?.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return mPluginActivity?.onCreateOptionsMenu(menu) == true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mPluginActivity?.onNewIntent(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mPluginActivity?.onSaveInstanceState(outState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mPluginActivity?.onSaveInstanceState(outState, outPersistentState)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        baseContext = newBase
    }

    override fun getResources(): Resources {
        if (isCallFromPlugin()) {
            if (mPluginActivity != null) {
                if (mPluginActivity?.pluginRes != null) {
                    return mPluginActivity?.pluginRes!!
                }
            }
        }
        return super.getResources()
    }

    private fun isCallFromPlugin(): Boolean {
        for (stackTraceElement in Thread.currentThread().stackTrace) {
            val cn = stackTraceElement.className
            if (cn.startsWith("android") || cn.startsWith(
                    "com.android"
                ) || cn.startsWith(
                    "java."
                ) || cn.startsWith(
                    "dalvik"
                )
            ) continue
            Log.d(
                TAG,
                "${stackTraceElement.className} ${stackTraceElement.methodName} method:${stackTraceElement.methodName} line:${stackTraceElement.lineNumber}"
            )

            if (pluginName == stackTraceElement.className) {
                return true
            }

        }
        return false
    }
}