package cc.test

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import cc.plugin.base.BasePluginActivity

class MainActivity : BasePluginActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.tv).setOnClickListener {
            Toast.makeText(this, "I click text", Toast.LENGTH_SHORT).show()
        }
        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://blog.csdn.net/xiaoy_yan/article/details/80985827")
    }
}