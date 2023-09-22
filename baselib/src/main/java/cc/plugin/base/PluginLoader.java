package cc.plugin.base;

import android.content.Context;
import android.util.Log;


import java.io.File;

import dalvik.system.DexClassLoader;

public class PluginLoader {
    private static final String TAG = "PluginLoader";

    public static DexClassLoader loadApk(Context context, String apkPath) {
        File outDir = new File(context.getFilesDir(), "plugin");
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File soDir = new File(outDir, "libs");
        if (!soDir.exists()) {
            soDir.mkdirs();
        }
        try {
            return new DexClassLoader(apkPath, outDir.getAbsolutePath(), soDir.getPath(), context.getClassLoader());
        } catch (Exception e) {
            Log.e(TAG, "loadApk: ", e);
        }
        return null;
    }

}
