package com.rootCauseMonitorSystem.client.impl;

import com.rootCauseMonitorSystem.client.PrometheusClient;
import com.rootCauseMonitorSystem.model.entity.MetricData;
import com.rootCauseMonitorSystem.model.response.MatrixPrometheusResponse;
import com.rootCauseMonitorSystem.model.response.PrometheusResponse;
import com.rootCauseMonitorSystem.model.response.VectorPrometheusResponse;
import com.rootCauseMonitorSystem.tools.TimeTool;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Data
@Component
public class PrometheusClientImpl implements PrometheusClient {
    @Resource
    private PrometheusApiClientImpl prometheusApiClient;

    @Resource
    private TimeTool timeTool;


    /**
     * Get the current metric target for the specified metric and label configuration.
     * :param metric_name: (str) The name of the metric
     * :param label_config: (dict) A dictionary that specifies metric labels and their values
     * :param min_value; filter the sequence whose value is greater than min_value
     * :param max_value; filter the sequence whose value is less than max_value
     * :param params: (dict) Optional dictionary containing GET parameters to be sent
     *     along with the API request, such as "time"
     * :returns: (list) A list of current metric values for the specified metric
     * :raises:
     *     (RequestException) Raises an exception in case of a connection error
     *     (ApiClientException) Raises in case of non 200 response status code
     */
    @Override
    public MetricData getCurrentMetricValue(String promql, Map<String, String> labelConfig,
                                            Double minValue, Double maxValue, Map<String, String> params) throws IOException {
        /**
         * params: now just choose `time`, `timeout`
         */
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(promql);
        if (!labelConfig.isEmpty()) {
            queryBuilder.append(labelToQuery(labelConfig));
        }
        if (minValue != null) {
            queryBuilder.insert(0, minValue + "<=");
        }
        if (maxValue != null) {
            queryBuilder.append("<").append(maxValue);
        }
        VectorPrometheusResponse response = null;
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("query", queryBuilder.toString());
        response = prometheusApiClient.query(params);
        // response.showResult();
        return integrateCurrentResult(promql, params, response);
    }

    MetricData integrateCurrentResult(String promql, Map<String, String> params, VectorPrometheusResponse response) {
        MetricData metricData = new MetricData();
        metricData.setPromql(promql);
        metricData.setStartTime(params.get("time"));
        metricData.setEndTime(params.get("time"));
        ArrayList<Double> timeRes = new ArrayList<>();
        ArrayList<Double> valueRes = new ArrayList<>();
        HashMap<String, ArrayList<String>> metricRes = new HashMap<>();
        for (VectorPrometheusResponse.VectorResult vectorResult : response.getData().getResult()) {
            timeRes.add(vectorResult.getValue().get(0));
            valueRes.add(vectorResult.getValue().get(1));
            vectorResult.getMetric().forEach((key, value) -> {
                metricRes.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            });
        }
        metricData.setTimeRes(timeRes);
        metricData.setValueRes(valueRes);
        metricData.setMetricRes(metricRes);
        return metricData;
    }

    MetricData integrateRangeResult(String promql, String startTime, String endTime, MatrixPrometheusResponse response) {
        MetricData metricData = new MetricData();
        metricData.setPromql(promql);
        metricData.setStartTime(startTime);
        metricData.setEndTime(endTime);
        ArrayList<Double> timeRes = new ArrayList<>();
        ArrayList<Double> valueRes = new ArrayList<>();
        HashMap<String, ArrayList<String>> metricRes = new HashMap<>();
        for (MatrixPrometheusResponse.MatrixResult matrixResult : response.getData().getResult()) {
            matrixResult.getValues().forEach((valueLis) -> {
                timeRes.add(valueLis.get(0));
                valueRes.add(valueLis.get(1));
            });
            matrixResult.getMetric().forEach((key, value) -> {
                metricRes.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            });
        }
        metricData.setTimeRes(timeRes);
        metricData.setValueRes(valueRes);
        metricData.setMetricRes(metricRes);
        return metricData;
    }

    /**
     * Get the current metric target for the specified metric and label configuration.
     * :param metric_name: (str) The name of the metric.
     * :param label_config: (dict) A dictionary specifying metric labels and their
     *             values.
     * :param start_time:  (datetime) A datetime object that specifies the metric range start time.
     * :param end_time: (datetime) A datetime object that specifies the metric range end time.
     * :param chunk_size: (timedelta) Duration of metric data downloaded in one request. For
     *     example, setting it to timedelta(hours=3) will download 3 hours worth of data in each
     *     request made to the prometheus host
     * :param step: (str) Query resolution step width in duration format or float number of seconds
     * :param min_value; filter the sequence whose value is greater than min_value
     * :param max_value; filter the sequence whose value is less than max_value
     * :param params: (dict) Optional dictionary containing GET parameters to be
     *     sent along with the API request, such as "time"
     * :return: (list) A list of metric data for the specified metric in the given time range
     * :raises:
     *     (RequestException) Raises an exception in case of a connection error
     *     (ApiClientException) Raises in case of non 200 response status code
     */
    @Override
    public MetricData getMetricRangeData(String promql, Map<String, String> labelConfig,
                                   String startTime, String endTime, String step,
                                   Double minValue, Double maxValue, Map<String, String> params) throws IOException {
        /**
         * params: now just choose `timeout`
         */
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(promql);
        if (!labelConfig.isEmpty()) {
            queryBuilder.append(labelToQuery(labelConfig));
        }
        long chunkSeconds = Duration.between(timeTool.string2LocalDataTime(startTime), timeTool.string2LocalDataTime(endTime)).getSeconds();
        if (step == null) {
            queryBuilder.append("[").append(chunkSeconds).append("s]");
            params.put("query", queryBuilder.toString());
            params.put("time", String.valueOf(endTime));
            VectorPrometheusResponse response;
            try {
                response = prometheusApiClient.query(params);
            } catch (IOException e) {
                System.out.println("find error: " + e.getMessage());
                throw new RuntimeException(e);
            }
            response.showResult();
            return integrateCurrentResult(promql, params, response);
        } else {
            if (minValue != null) {
                queryBuilder.insert(0, minValue + "<=");
            }
            if (maxValue != null) {
                queryBuilder.append("<").append(maxValue);
            }
            params.put("query", queryBuilder.toString());
            params.put("start", startTime);
            params.put("end", endTime);
            params.put("step", step);
            MatrixPrometheusResponse response;
            response = prometheusApiClient.queryRange(params);
            // response.showResult();
            return integrateRangeResult(promql, startTime, endTime, response);
        }
    }

    public static String labelToQuery(Map<String, String> labels) {
        List<String> queryList = new ArrayList<>();
        if (labels != null && !labels.isEmpty()) {
            labels.forEach((key, value) -> {
                queryList.add(key + "=\"" + value + "\"");
            });
        }
        return "{" + String.join(",", queryList) + "}";
    }
}
