package com.example.apphydrojetting.activity.ui.about;

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
import com.example.apphydrojetting.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AboutViewModel aboutViewModel =
                new ViewModelProvider(this).get(AboutViewModel.class);

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.titleAboutUs;
        aboutViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Configura el WebView Caja comentarios
        WebView webView = binding.webviewBox;
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://birdeye.com/widget/render.php?bid=161912591154571&wid=3&ver=4&update=0"); // Reemplaza con la URL deseada

        // Configura el WebView Localizacion
        WebView webviewLocalization = binding.webviewLocalization;
        webSettings = webviewLocalization.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webviewLocalization.setWebViewClient(new WebViewClient());
        webviewLocalization.loadUrl("https://maps.google.com/?q=Patrick%20Riley%20%7C%20Isley%27s%20Cooling%2C%20Heating%2C%20Plumbing%20%26%20Electrical%201850%20E%20Watkins%20St%2C%20Phoenix%2C%20AZ%2C%2085034"); // Reemplaza con la URL deseada

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}