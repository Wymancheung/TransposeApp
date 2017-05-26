//# COMP 4521    #  CHEUNG, Wai Man Raymond   20199778   wmcheungaa@connect.ust.hk
//# COMP 4521    #  LAW, Chiu Kwan  20212087   cklawad@connect.ust.hk
//# COMP 4521    #  WONG, Ho Yin Calvin  20196726  hycwong@connect.ust.hk
package com.comp4521gp01.transposeapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

/**
 * Created by waimancheung on 4/4/2017.
 */

public class WebActivity extends Activity{

    public static final String EXTRA_MESSAGE = "MESSAGETOCROP";
    private WebView webView;
    private EditText editTextURL;
    private ImageButton leave_web,page_previous, page_forward, go_url, refresh_url, crop_web, del_url;
    private Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();

        editTextURL =  (EditText) findViewById(R.id.edittext_url);
        leave_web = (ImageButton) findViewById(R.id.leave_web);
        page_previous = (ImageButton) findViewById(R.id.page_previous);
        page_forward = (ImageButton) findViewById(R.id.page_forward);
        go_url = (ImageButton) findViewById(R.id.go_url);
        refresh_url = (ImageButton) findViewById(R.id.refresh_url);
        crop_web = (ImageButton) findViewById(R.id.crop_web);
        del_url = (ImageButton) findViewById(R.id.del_url);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com.hk");

        editTextURL.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                GoURL();
                return false;
            }
        });
        leave_web.setOnClickListener(clickListener);
        page_previous.setOnClickListener(clickListener);
        page_forward.setOnClickListener(clickListener);
        go_url.setOnClickListener(clickListener);
        refresh_url.setOnClickListener(clickListener);
        crop_web.setOnClickListener(clickListener);
        del_url.setOnClickListener(clickListener);


    }

    private View.OnClickListener clickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.leave_web:
                    Intent intent = new Intent();
                    intent.putExtra("Back", 0);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case R.id.page_previous:
                    webView.goBack();
                    break;
                case R.id.page_forward:
                    webView.goForward();
                    break;
                case R.id.go_url:
                    GoURL();
                    break;
                case R.id.refresh_url:
                    if(refresh_url.getDrawable() == getResources().getDrawable(R.drawable.remove)){
                        webView.stopLoading();
                    }else {
                        WebActivity.this.webView.loadUrl(editTextURL.getText().toString());
                    }
                    break;
                case R.id.del_url:
                    editTextURL.setText("");
                    break;
                case R.id.crop_web:

                    webView.setDrawingCacheEnabled(true);
                    bitmap = webView.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Intent intentCali = new Intent(WebActivity.this, CropActivity.class);
                    intentCali.putExtra("picture", byteArray);
                    startActivity(intentCali);
                    finish();
                    break;
            }
        }
    };

    private void GoURL(){
        if(editTextURL.getText().toString().startsWith("https://")){
            webView.loadUrl(editTextURL.getText().toString());
        }else if(editTextURL.getText().toString().startsWith("www.")){
            webView.loadUrl("https://" + editTextURL.getText().toString());
        }else {
            webView.loadUrl("https://www.google.com.hk/search?q=" + editTextURL.getText().toString());
        }
    }

    private class WebViewClient extends android.webkit.WebViewClient{
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            editTextURL.setText(webView.getUrl());
            refresh_url.setImageDrawable(getResources().getDrawable(R.drawable.remove));

        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            editTextURL.setText(webView.getUrl());
            refresh_url.setImageDrawable(getResources().getDrawable(R.drawable.sync));
            ChangePages();
        }

    }

    private void ChangePages(){
        if(webView.canGoBack()){
            page_previous.setImageDrawable(getResources().getDrawable(R.drawable.left));
        }else{
            page_previous.setImageDrawable(getResources().getDrawable(R.drawable.left_dis));
        }

        if(webView.canGoForward()){
            page_forward.setImageDrawable(getResources().getDrawable(R.drawable.right));
        }else{
            page_forward.setImageDrawable(getResources().getDrawable(R.drawable.right_dis));
        }

    }
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            Intent intent = new Intent();
            intent.putExtra("Back", 0);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
