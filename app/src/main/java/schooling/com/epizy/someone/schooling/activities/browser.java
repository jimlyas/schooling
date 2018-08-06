package schooling.com.epizy.someone.schooling.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.util.Objects;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import schooling.com.epizy.someone.schooling.R;

public class browser extends AppCompatActivity {
    Toolbar tb; WebView wb; SmoothProgressBar spb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_browser);
        tb = findViewById(R.id.toolbar_browser);
        wb = findViewById(R.id.web_view);
        spb = findViewById(R.id.ProgressBarBrowser);

        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_clear);

        initWeb();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb() {
        wb.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                tb.setTitle("Loading Page. . .");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                tb.setSubtitle(view.getUrl());
                tb.setTitle(view.getTitle().replace("GitHub - ", ""));
                ((LinearLayout)findViewById(R.id.linear_browser)).removeView(spb);
            }

        });
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            this.onBackPressed();
        }else if(item.getItemId()==R.id.menu_browser_copy){
            ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("Url-Link", tb.getSubtitle());
            clip.setPrimaryClip(data);
            Snackbar.make(findViewById(R.id.root_browser), "Link copied!", Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
