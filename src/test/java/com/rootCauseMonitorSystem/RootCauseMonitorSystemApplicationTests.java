package com.rootCauseMonitorSystem;

import com.rootCauseMonitorSystem.client.impl.PrometheusApiClientImpl;
import com.rootCauseMonitorSystem.client.impl.PrometheusClientImpl;
import com.rootCauseMonitorSystem.model.ConstantRepository;
import com.rootCauseMonitorSystem.model.entity.MetricConfig;
import com.rootCauseMonitorSystem.tools.FileTool;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;

@SpringBootTest
class RootCauseMonitorSystemApplicationTests {
    @Resource
    private FileTool fileTool;

    @Resource
    private PrometheusApiClientImpl prometheusApiClient;

    @Resource
    private PrometheusClientImpl prometheusClient;

    // @Test
    // public void testQuery() throws IOException {
    //     VectorPrometheusResponse response = prometheusApiClient.query("sum(node_cpu_seconds_total)");
    //     VectorPrometheusResponse.VectorData data = response.data;
    //     System.out.println("response = " + data.result.toString());
    // }



    @Test
    void testCurrentMetricValue() {
        HashMap<String, String> metricMap = fileTool.readSimpleConfigFile(ConstantRepository.METRIC_MAP_CONFIG);
        HashMap<String, MetricConfig> parsePrometheusMetrics = fileTool.parsePrometheusMetrics(ConstantRepository.REPROCESSING_EXPORTER_CONFIG, metricMap);
        String promql = parsePrometheusMetrics.get("os_cpu_user_usage").getPromql();
        System.out.println("promql: " + promql);
        prometheusClient.getCurrentMetricValue(promql, null, 0.1, 0.3, null);
    }

    @Test
    void testMetricRangeData() {
        // HashMap<String, String> metricMap = fileTool.readSimpleConfigFile(ConstantRepository.METRIC_MAP_CONFIG);
        // HashMap<String, MetricConfig> parsePrometheusMetrics = fileTool.parsePrometheusMetrics(ConstantRepository.REPROCESSING_EXPORTER_CONFIG, metricMap);
        // String promql = parsePrometheusMetrics.get("os_cpu_user_usage").getPromql();
        // System.out.println("promql: " + promql);
        // double startTime = (double) System.currentTimeMillis() / 1000 - 60 * 60;
        // double endTime = (double) System.currentTimeMillis() / 1000;
        // LocalDateTime startDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli((long) (startTime * 1000)), ZoneId.systemDefault());
        // LocalDateTime endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli((long) (endTime * 1000)), ZoneId.systemDefault());
        // prometheusClient.getMetricRangeData(promql, null, startDateTime, endDateTime,
        //         "300", null, null, null);
    }
}
