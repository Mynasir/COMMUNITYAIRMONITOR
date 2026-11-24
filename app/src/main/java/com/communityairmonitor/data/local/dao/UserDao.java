package com.communityairmonitor.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.communityairmonitor.data.local.entity.UserEntity;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    LiveData<UserEntity> getUserProfile();

    @Query("SELECT * FROM users LIMIT 1")
    UserEntity getUserProfileSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(UserEntity user);

    @Update
    void update(UserEntity user);
}
