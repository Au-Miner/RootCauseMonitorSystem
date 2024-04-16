package com.rootCauseMonitorSystem.model.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class KeyValPrometheusResponse extends PrometheusResponse{
    List<Map<String, String>> data;

    @Override
    public void showResult() {
        System.out.println("data.result: " + data.toString());
    }
}
