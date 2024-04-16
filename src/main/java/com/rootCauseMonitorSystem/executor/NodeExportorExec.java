package com.rootCauseMonitorSystem.executor;

import com.rootCauseMonitorSystem.model.entity.MetricData;

import java.util.Map;

public interface NodeExportorExec {

    void init();

    void setMinValue(Double value);

    void setMaxValue(Double value);

    void addLabelSelector(String labelName, String labelValue);

    void delLabelSelector(String labelName, String labelValue);

    Map<String, MetricData> getNodeMetricData(String startTime, String endTime, String step);
}
