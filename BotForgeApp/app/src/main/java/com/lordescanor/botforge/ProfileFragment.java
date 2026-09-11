package com.lordescanor.botforge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends androidx.fragment.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater i, ViewGroup p, Bundle b) {
        View v = i.inflate(R.layout.fragment_profile, p, false);
        TextView name = v.findViewById(R.id.profileName);
        TextView email = v.findViewById(R.id.profileEmail);
        name.setText("LORD ESCANOR");
        email.setText("حساب محلي");
        v.findViewById(R.id.logout).setOnClickListener(x -> {
            SharedPreferences sp = requireActivity().getSharedPreferences("botforge_auth", 0);
            sp.edit().clear().apply();
            startActivity(new Intent(requireActivity(), SplashActivity.class));
            requireActivity().finish();
        });
        return v;
    }
}
