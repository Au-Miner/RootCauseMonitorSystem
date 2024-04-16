package com.rootCauseMonitorSystem.model.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class VectorPrometheusResponse extends PrometheusResponse{
    VectorData data;

    @Data
    public class VectorData {
        String resultType;
        List<VectorResult> result;
    }

    @Data
    public class VectorResult {
        Map<String, String> metric;
        List<Double> value;

        @Override
        public String toString() {
            return String.format(
                    "metric: %s\nvalue: %s",
                    metric.toString(),
                    value == null ? "" : value.toString()
            );
        }
    }

    @Override
    public void showResult() {
        System.out.println("data.resultType: " + data.resultType);
        System.out.println("data.result: " + data.result.toString());
    }
}
