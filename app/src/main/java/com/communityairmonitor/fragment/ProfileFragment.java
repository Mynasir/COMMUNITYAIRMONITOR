package com.communityairmonitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.communityairmonitor.R;
import com.communityairmonitor.ui.profile.EditProfileActivity;
import com.communityairmonitor.ui.settings.NotificationSettingsActivity;
import com.communityairmonitor.ui.settings.SettingsActivity;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.rowEditProfile).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.rowNotificationSettings).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), NotificationSettingsActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.rowSecurity).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }
}
