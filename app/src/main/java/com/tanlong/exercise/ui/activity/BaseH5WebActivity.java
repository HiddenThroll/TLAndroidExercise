package com.tanlong.exercise.ui.activity;

import android.databinding.DataBindingUtil;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;
import com.tanlong.exercise.R;
import com.tanlong.exercise.databinding.ActivityH5WebviewBinding;
import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.VersionUtil;

/**
 * 基本H5 Web界面
 *
 * @author Administrator
 */
public class BaseH5WebActivity extends BaseActivity {

    private ActivityH5WebviewBinding binding;
    private WebView mWebView;
    private String url = "http://m.qiaocat.com/topic-618_topic/topicIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_h5_webview);
        initArgs();
        initWebView();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            binding.llContainer.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            Logger.e("can back");
            mWebView.goBack();
        } else {
            Logger.e("can not back");
            finish();
        }
    }

    private void initArgs() {

    }

    private void initWebView() {
        //手动添加WebView,防止内存泄露
        mWebView = new WebView(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        binding.llContainer.addView(mWebView);

        initWebViewSetting(mWebView);
        initWebClient(mWebView);
    }

    private void initWebViewSetting(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        //支持JavaScript
        webSettings.setJavaScriptEnabled(true);

        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");

        //自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //不支持缩放
        webSettings.setSupportZoom(false);
        //缓存策略
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //不允许访问文件
        webSettings.setAllowFileAccess(false);
        //可以通过JavaScript打开窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");
    }

    private void initWebClient(WebView webView) {
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //等待证书响应
                Logger.e("等待证书响应");
                handler.proceed();
//                super.onReceivedSslError(view, delayHandler, error);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 获取页面内容
//                view.loadUrl("javascript:window.java_obj.showSource("
//                        + "document.getElementsByTagName('html')[0].innerHTML);");
                view.loadUrl("javascript:window.java_obj.showSource("
                        + "document.getElementsByTagName('title')[0].innerHTML);");
                // 获取解析<meta name="share-description" content="获取到的值">
                view.loadUrl("javascript:window.java_obj.showDescription("
                        + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')"
                        + ");");
                super.onPageFinished(view, url);
            }
        });
    }

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.e("test", "html is " + html);
        }

        @JavascriptInterface
        public void showDescription(String str) {
            Log.e("test", "str is " + str);
        }
    }
}
