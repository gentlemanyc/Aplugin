package cc.plugin.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.arch.core.util.Function
import java.io.File
import java.util.concurrent.Executors

private val exe = Executors.newCachedThreadPool()
private const val TAG = "PluginManager"

data class PluginInfo(val name: String, val apkPath: String, val classLoader: ClassLoader)
object PluginManager {
    private val mClassLoaderMap: MutableMap<String, PluginInfo> = HashMap()
    private val handler = Handler(Looper.getMainLooper())

    @SuppressLint("StaticFieldLeak")
    fun installPlugin(
        context: Context?,
        pluginName: String,
        apkPath: String,
        function: Function<Boolean, Any?>?,
    ) {
        exe.submit {
            if (mClassLoaderMap.containsKey(pluginName)) {
                function?.apply(true)
                return@submit
            }
            val loader: ClassLoader? = PluginLoader.loadApk(context, apkPath)
            if (loader != null) {
                val pluginInfo = PluginInfo(pluginName, apkPath, loader)
                mClassLoaderMap[pluginName] = pluginInfo
            }
            handler.post {
                function?.apply(loader != null)
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    fun installPluginFromAssets(
        context: Context,
        pluginName: String,
        assetsName: String,
        function: Function<Boolean, Any?>?,
    ) {
        exe.submit {
            val apkFile = File(context.filesDir, "plugin.apk")
            apkFile.writeBytes(context.assets.open(assetsName).readBytes())
            installPlugin(context, pluginName, apkFile.absolutePath, function)
        }
    }

    fun startActivity(context: Context, pluginName: String, className: String?, bundle: Bundle?) {
        val intent = Intent(context, DefStubActivity::class.java)
        intent.putExtra(Constants.KEY_CLASS_NAME, className)
        intent.putExtra(Constants.KEY_PLUGIN_NAME, pluginName)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        context.startActivity(intent)
    }


    fun loadClass(pluginName: String, clsName: String?): Class<*>? {
        val pluginInfo = mClassLoaderMap[pluginName]
        if (pluginInfo?.classLoader != null) {
            try {
                return pluginInfo.classLoader.loadClass(clsName)
            } catch (e: ClassNotFoundException) {
                Log.e(TAG, "loadClass: ", e)
            }
        }
        return null
    }

    fun getPluginInfo(pluginName: String): PluginInfo? {
        return mClassLoaderMap[pluginName]
    }
}