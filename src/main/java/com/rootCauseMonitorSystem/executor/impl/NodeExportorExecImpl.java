package com.rootCauseMonitorSystem.executor.impl;

import com.rootCauseMonitorSystem.client.PrometheusClient;
import com.rootCauseMonitorSystem.executor.NodeExportorExec;
import com.rootCauseMonitorSystem.model.ConstantRepository;
import com.rootCauseMonitorSystem.model.entity.MetricConfig;
import com.rootCauseMonitorSystem.model.entity.MetricData;
import com.rootCauseMonitorSystem.tools.FileTool;
import com.rootCauseMonitorSystem.tools.TimeTool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class NodeExportorExecImpl implements NodeExportorExec {
    // e.x. os_cpu_usage -> os_cpu_user_usage
    private HashMap<String, String> metricMap;
    // e.x. os_cpu_user_usage -> MetricConfig
    private HashMap<String, MetricConfig> parsePrometheusMetrics;
    private HashMap<String, String> labelSelector = new HashMap<>();

    private Double minValue;
    private Double maxValue;

    @Resource
    private FileTool fileTool;

    @Resource
    private TimeTool timeTool;

    @Resource
    private PrometheusClient prometheusClient;

    @Override
    public void init() {
        this.metricMap = fileTool.readSimpleConfigFile(ConstantRepository.METRIC_MAP_CONFIG);
        this.parsePrometheusMetrics = fileTool.parsePrometheusMetrics(ConstantRepository.REPROCESSING_EXPORTER_CONFIG, metricMap);
    }

    @Override
    public void setMinValue(Double value) {
        this.minValue = value;
    }

    @Override
    public void setMaxValue(Double value) {
        this.maxValue = value;
    }

    /**
     * define the
     * @param labelName: label name
     * @param labelValue: selected value
     */
    @Override
    public void addLabelSelector(String labelName, String labelValue) {
        this.labelSelector.put(labelName, labelValue);
    }

    @Override
    public void delLabelSelector(String labelName, String labelValue) {
        this.labelSelector.remove(labelName, labelValue);
    }

    @Override
    public Map<String, MetricData> getNodeMetricData(String startTime, String endTime, String step) {
        HashMap<String, MetricData> nodeMetricDataMap = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        this.parsePrometheusMetrics.forEach((metricName, metricConfig) -> {
            params.clear();
            String promql = parsePrometheusMetrics.get(metricName).getPromql();
            MetricData metricData = new MetricData();
            if (startTime.equals(endTime) || (Double.parseDouble(endTime) - Double.parseDouble(startTime)) < 1.0) {
                params.put("time", startTime);
                if (parsePrometheusMetrics.get(metricName).getTimeout() != null) {
                    params.put("timeout", String.valueOf(parsePrometheusMetrics.get(metricName).getTimeout()));
                }
                try {
                    metricData = prometheusClient.getCurrentMetricValue(promql, this.labelSelector, this.minValue, this.maxValue, params);
                } catch (IOException e) {
                    log.error("Prometheus API invoking error");
                    throw new RuntimeException(e);
                }
                metricData.setIfCurrentMetricValue(true);
            } else {
                if (parsePrometheusMetrics.get(metricName).getTimeout() != null) {
                    params.put("timeout", String.valueOf(parsePrometheusMetrics.get(metricName).getTimeout()));
                }
                try {
                    metricData = prometheusClient.getMetricRangeData(promql, this.labelSelector, startTime, endTime,
                            step, this.minValue, this.maxValue, params);
                } catch (IOException e) {
                    log.error("Prometheus API invoking error");
                    throw new RuntimeException(e);
                }
            }
            metricData.setName(metricName);
            nodeMetricDataMap.put(metricName, metricData);
        });
        return nodeMetricDataMap;
    }
}
