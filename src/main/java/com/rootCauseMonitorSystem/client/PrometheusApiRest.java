package com.rootCauseMonitorSystem.client;


import com.rootCauseMonitorSystem.model.response.KeyValPrometheusResponse;
import com.rootCauseMonitorSystem.model.response.MatrixPrometheusResponse;
import com.rootCauseMonitorSystem.model.response.VectorPrometheusResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface PrometheusApiRest {
    @GET("api/v1/query")
    Call<VectorPrometheusResponse> query(
        @Query("query") String query,
        @Query("time") String time,
        @Query("timeout") String timeout
    );

    @GET("api/v1/query_range")
    Call<MatrixPrometheusResponse> queryRange(
        @Query("query") String query,
        @Query("start") String start,
        @Query("end") String end,
        @Query("step") String step,
        @Query("timeout") String timeout
    );

    @GET("api/v1/series")
    Call<KeyValPrometheusResponse> findSeries(
        @Query("match[]") String match,
        @Query("start") String start,
        @Query("end") String end
    );
}
