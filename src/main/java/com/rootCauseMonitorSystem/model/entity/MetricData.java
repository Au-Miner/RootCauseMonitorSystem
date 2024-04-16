package com.rootCauseMonitorSystem.model.entity;

import com.rootCauseMonitorSystem.client.impl.PrometheusClientImpl;
import jakarta.annotation.Resource;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MetricData {
    private String name;
    private String promql;
    private Boolean ifCurrentMetricValue;
    private String startTime;
    private String endTime;
    private List<Double> timeRes = new ArrayList<>();
    private List<Double> valueRes = new ArrayList<>();
    // e.x. instance=10.214.151.191 job=node_exporter
    private HashMap<String, ArrayList<String>> metricRes = new HashMap<>();
}

