package com.rootCauseMonitorSystem.client;

import com.rootCauseMonitorSystem.model.response.MatrixPrometheusResponse;
import com.rootCauseMonitorSystem.model.response.VectorPrometheusResponse;

import java.io.IOException;
import java.util.Map;

public interface PrometheusApiClient {
    VectorPrometheusResponse query(Map<String, String> params) throws IOException;

    MatrixPrometheusResponse queryRange(Map<String, String> params) throws IOException;
}
