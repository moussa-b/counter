package com.bdzapps.counterpp.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bdzapps.counterpp.commons.Utils;
import com.mbo.counter.R;

public class WebViewActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_activity);

        WebViewFragment webViewFragment = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (webViewFragment == null)
        {
            String url = null;
            if (getIntent().hasExtra(WebViewFragment.ARGUMENT_LOAD_URL))
                url = getIntent().getStringExtra(WebViewFragment.ARGUMENT_LOAD_URL);

            if (url != null)
            {
                Bundle bundle = new Bundle();
                bundle.putString(WebViewFragment.ARGUMENT_LOAD_URL, url);
                webViewFragment = WebViewFragment.newInstance();
                webViewFragment.setArguments(bundle);
                Utils.addFragmentToActivity(getSupportFragmentManager(), webViewFragment, R.id.contentFrame);
            }
            else
                finish();
        }
    }
}
