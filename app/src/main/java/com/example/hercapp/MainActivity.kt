package com.example.hercapp

import android.os.Bundle
import android.view.View
import android.webkit.WebView;
import android.webkit.WebResourceError;
import android.webkit.WebViewClient
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.example.hercapp.databinding.ActivityMainBinding
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webView = binding.webView
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView?, request: android.webkit.WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                webView.setBackgroundColor(Color.GRAY)
                showErrorDialog(view)
            }

            @Suppress("DEPRECATION") // For compatibility with older Android versions
            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                webView.setBackgroundColor(Color.GRAY)
                showErrorDialog(view)
            }

            private fun showErrorDialog(webView: WebView?) {
                binding.errorOverlay.visibility = View.VISIBLE // 显示覆盖层

                // 如果使用onClick属性，则不需要下面的代码
                // 但为了演示，我们在这里设置一个监听器（通常不推荐与onClick属性一起使用）
                binding.btnRefresh.setOnClickListener {
                    webView?.reload() // 尝试重新加载
                    binding.errorOverlay.visibility = View.GONE // 隐藏覆盖层
                }

//                webView?.visibility = View.GONE
//                AlertDialog.Builder(this@MainActivity)
//                    .setTitle("网络错误")
//                    .setMessage("网络信号不好，请检查您的网络连接。")
//                    .setPositiveButton("刷新", { _, _ ->
//                        webView?.visibility = View.VISIBLE
//                        webView?.reload()
//                    }).setNegativeButton("取消", { dialog, _ ->
//                        dialog.dismiss() // 关闭对话框
//                    })
//                    .show()
            }
        }

        webView.loadUrl("https://www.baidu.com")
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        // 设置WebView的canGoBack作为返回键的监听条件
        webView.setOnKeyListener { _, keyCode, event ->
            if (event.action == android.view.KeyEvent.ACTION_DOWN && keyCode == android.view.KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                webView.goBack() // 回退到上一个页面
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }


    // 如果不需要重写onBackPressed来处理WebView的返回逻辑，可以保留默认实现
    // 或者，如果您想在Activity级别也处理返回键（比如当WebView没有可回退页面时），可以取消注释并重写此方法
    override fun onBackPressed() {
        if (!binding.webView.canGoBack()) {
            // WebView没有可回退的页面，可以选择关闭Activity
            // 显示确认对话框
            AlertDialog.Builder(this)
                .setTitle("确认退出")
                .setMessage("您确定要退出吗？")
                .setPositiveButton("确定") { _, _ ->
                    // 用户点击了确定，退出Activity
                    super.onBackPressed()
                }
                .setNegativeButton("取消", null)
                .show()
            // 如果不需要确认对话框，则直接调用super.onBackPressed()来退出Activity
            // super.onBackPressed()
        }
    }

}