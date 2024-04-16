package com.rootCauseMonitorSystem.model.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MatrixPrometheusResponse extends PrometheusResponse {
    MatrixData data;

    @Data
    public class MatrixData {
        String resultType;
        List<MatrixResult> result;
    }

    @Data
    public class MatrixResult {
        Map<String, String> metric;
        List<List<Double>> values;

        @Override
        public String toString() {
            return String.format(
                "metric: %s\nvalues: %s",
                metric.toString(),
                values == null ? "" : values.toString()
            );
        }
    }

    @Override
    public void showResult() {
        System.out.println("data.resultType: " + data.resultType);
        System.out.println("data.result: " + data.result.toString());
    }
}
