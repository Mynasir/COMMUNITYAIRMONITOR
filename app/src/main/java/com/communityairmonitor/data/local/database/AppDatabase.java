package com.communityairmonitor.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.communityairmonitor.data.local.dao.AlertDao;
import com.communityairmonitor.data.local.dao.PredictionDao;
import com.communityairmonitor.data.local.dao.SensorDao;
import com.communityairmonitor.data.local.dao.SensorReadingDao;
import com.communityairmonitor.data.local.dao.UserDao;
import com.communityairmonitor.data.local.entity.AlertEntity;
import com.communityairmonitor.data.local.entity.PredictionEntity;
import com.communityairmonitor.data.local.entity.SensorEntity;
import com.communityairmonitor.data.local.entity.SensorReadingEntity;
import com.communityairmonitor.data.local.entity.UserEntity;

@Database(entities = {
        SensorEntity.class,
        SensorReadingEntity.class,
        AlertEntity.class,
        UserEntity.class,
        PredictionEntity.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "caqmn_database.db";
    private static volatile AppDatabase instance;

    public abstract SensorDao sensorDao();
    public abstract SensorReadingDao sensorReadingDao();
    public abstract AlertDao alertDao();
    public abstract UserDao userDao();
    public abstract PredictionDao predictionDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
