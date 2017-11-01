package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;


import com.writm.R;

import Utils.TypefaceSpan;

/**
 * Created by shash on 8/24/2017.
 */

public class PrivacyFragment extends Fragment {

    WebView webView;
    ProgressBar progressBar;

    public static PrivacyFragment newInstance() {
        PrivacyFragment fragment = new PrivacyFragment();
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Privacy Policy");
        s.setSpan(new TypefaceSpan( getActivity(), "josephbold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getActivity().setTitle(s);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view =  inflater.inflate(R.layout.privacy_policy, container, false);
        progressBar= (ProgressBar) view.findViewById(R.id.progressbar_imageloading);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, 3500);
        webView= (WebView) view.findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://writm.com/privacy-policy/");
        return view;
    }
}
