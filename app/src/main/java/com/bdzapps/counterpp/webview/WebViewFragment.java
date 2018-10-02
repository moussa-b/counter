package com.bdzapps.counterpp.webview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.mbo.counter.R;

public class WebViewFragment extends Fragment
{
    public static final String ARGUMENT_LOAD_URL = "ARGUMENT_LOAD_URL";

    public static WebViewFragment newInstance()
    {
        return new WebViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.web_view_fragment, container, false);
        WebView webView = root.findViewById(R.id.web_view);
        if (getArguments() != null)
        {
            String url = getArguments().getString(ARGUMENT_LOAD_URL);
            webView.loadUrl(url);
        }

        return root;
    }
}
