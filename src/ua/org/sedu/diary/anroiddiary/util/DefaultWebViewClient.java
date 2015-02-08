/**
 *
 */
package ua.org.sedu.diary.anroiddiary.util;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author voinovdenys
 */
public class DefaultWebViewClient extends WebViewClient {

    private final ProgressDialog progressBar;

    public DefaultWebViewClient(ProgressDialog progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        progressBar.show();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        progressBar.dismiss();
    }

}
