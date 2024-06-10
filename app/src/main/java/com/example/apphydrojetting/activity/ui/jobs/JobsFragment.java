package com.example.apphydrojetting.activity.ui.jobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphydrojetting.databinding.FragmentJobsBinding;

public class JobsFragment extends Fragment {

    private FragmentJobsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        JobsViewModel jobsViewModel =
                new ViewModelProvider(this).get(JobsViewModel.class);

        binding = FragmentJobsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textJobTitle;
        jobsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Configura el WebView
        WebView webView = binding.webview;
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://birdeye.com/widget/render.php?bid=161912591154571&wid=3&ver=4&update=0"); // Reemplaza con la URL deseada

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}