package com.rootCauseMonitorSystem.model.response;

import lombok.Data;

import java.util.List;

@Data
public abstract class PrometheusResponse {
    String status;

    public abstract void showResult();
}
