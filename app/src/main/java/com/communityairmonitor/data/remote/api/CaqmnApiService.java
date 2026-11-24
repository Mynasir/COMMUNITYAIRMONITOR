package com.communityairmonitor.data.remote.api;

import com.communityairmonitor.data.local.entity.AlertEntity;
import com.communityairmonitor.data.local.entity.PredictionEntity;
import com.communityairmonitor.data.local.entity.SensorEntity;
import com.communityairmonitor.data.local.entity.SensorReadingEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CaqmnApiService {

    @GET("api/v1/sensors")
    Call<List<SensorEntity>> getSensors();

    @POST("api/v1/sensors")
    Call<SensorEntity> createSensor(@Body SensorEntity sensor);

    @GET("api/v1/sensors/{id}/readings")
    Call<List<SensorReadingEntity>> getSensorReadings(@Path("id") String sensorId);

    @GET("api/v1/alerts")
    Call<List<AlertEntity>> getAlerts();

    @GET("api/v1/predictions")
    Call<List<PredictionEntity>> getPredictions();
}
