package com.rootCauseMonitorSystem.client;

import com.rootCauseMonitorSystem.model.entity.MetricData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public interface PrometheusClient {
    MetricData getCurrentMetricValue(String promql, Map<String, String> labelConfig, Double minValue, Double maxValue, Map<String, String> params) throws IOException;

    MetricData getMetricRangeData(String promql, Map<String, String> labelConfig, String startTime, String endTime, String step, Double minValue, Double maxValue, Map<String, String> params) throws IOException;
}
