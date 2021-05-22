package com.mizix.tastynkrunchy.ui.devtools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mizix.tastynkrunchy.R;

public class DevToolsFragment extends Fragment {

    private DevToolsViewModel devToolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        devToolsViewModel =
                ViewModelProviders.of(this).get(DevToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_devtools, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        devToolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}