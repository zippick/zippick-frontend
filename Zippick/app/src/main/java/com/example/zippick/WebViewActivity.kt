package com.example.zippick

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun onAddressSelected(result: String) {
                Log.d("WebView", "선택된 주소: $result")
                val (zipcode, basic, extra) = result.split("|")
                val intent = Intent().apply {
                    putExtra("zipcode", zipcode)
                    putExtra("basicAddress", basic)
                    putExtra("extraAddress", extra)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }, "AndroidInterface")

        val html = assets.open("postcode.html").bufferedReader().use { it.readText() }
        webView.loadDataWithBaseURL(
            "https://t1.daumcdn.net",
            html,
            "text/html",
            "UTF-8",
            null
        )
    }
}
