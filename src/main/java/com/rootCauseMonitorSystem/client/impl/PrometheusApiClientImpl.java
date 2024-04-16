package com.rootCauseMonitorSystem.client.impl;


import com.rootCauseMonitorSystem.client.PrometheusApiClient;
import com.rootCauseMonitorSystem.client.PrometheusApiRest;
import com.rootCauseMonitorSystem.model.response.KeyValPrometheusResponse;
import com.rootCauseMonitorSystem.model.response.MatrixPrometheusResponse;
import com.rootCauseMonitorSystem.model.response.VectorPrometheusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Map;

@Component
public class PrometheusApiClientImpl implements PrometheusApiClient {

    public String baseUrl;
    private Retrofit retrofit;
    private PrometheusApiRest service;

    public PrometheusApiClientImpl(@Value("${prometheus.baseUrl}") String baseUrl) {
        this.baseUrl = baseUrl;
        System.out.println("this.baseUrl: " + this.baseUrl);
        this.retrofit = new Retrofit.Builder()
            .baseUrl(this.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        service = retrofit.create(PrometheusApiRest.class);
    }

    @Override
    public VectorPrometheusResponse query(Map<String, String> params) throws IOException {
        String query = params.get("query");
        String time = params.get("time");
        String timeout = params.get("timeout");
        System.out.println("query: " + query);
        System.out.println("time: " + time);
        System.out.println("timeout: " + timeout);
        return service.query(query, time, timeout).execute().body();
    }

    @Override
    public MatrixPrometheusResponse queryRange(Map<String, String> params) throws IOException {
        String query = params.get("query");
        String start = params.get("start");
        String end = params.get("end");
        String step = params.get("step");
        String timeout = params.get("timeout");
        System.out.println("query: " + query);
        System.out.println("start: " + start);
        System.out.println("end: " + end);
        System.out.println("step: " + step);
        System.out.println("timeout: " + timeout);
        return service.queryRange(query, start, end, step, timeout).execute().body();
    }

    public KeyValPrometheusResponse findSeries(String match, String start, String end) throws IOException {
        return service.findSeries(match, start, end).execute().body();
    }

}
