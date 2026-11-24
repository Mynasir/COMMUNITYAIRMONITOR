package com.communityairmonitor.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.communityairmonitor.data.local.entity.UserEntity;
import com.communityairmonitor.data.repository.CaqmnRepository;

public class ProfileViewModel extends AndroidViewModel {

    private final CaqmnRepository repository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = CaqmnRepository.getInstance(application);
    }

    public LiveData<UserEntity> getUserProfile() {
        return repository.getUserProfile();
    }

    public void updateUserProfile(UserEntity user) {
        repository.updateUserProfile(user);
    }
}
