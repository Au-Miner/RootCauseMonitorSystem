package com.rootCauseMonitorSystem.model.entity;

import com.rootCauseMonitorSystem.client.impl.PrometheusClientImpl;
import jakarta.annotation.Resource;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MetricConfig {
    private String name;
    private String desc;
    private String promql;
    private Integer ttl;
    private Integer timeout;
    private List<String> names = new ArrayList<>(); // name
    private Map<String, String> labelMap = new HashMap<>(); // label -> name

    private long expiredTime = 0;
    private String cachedResult;

    @Resource
    private PrometheusClientImpl prometheusClient;

    public MetricConfig(String name, String promql, String desc, Integer ttl, Integer timeout) {
        this.name = name;
        this.desc = desc;
        this.promql = promql;
        this.ttl = ttl;
        this.timeout = timeout;
    }

    public void addLabel(String label, String name) {
        names.add(name);
        labelMap.put(label, name);
    }

    public String getLabelName(String label) {
        return labelMap.get(label);
    }

    public List<String> getNames() {
        return names;
    }

    @Override
    public String toString() {
        return "(" + name + ", " + promql + ", " + names + ")";
    }


    // private String customQuery(String promql, Integer timeout) {
    //     return prometheusClient.customQuery(promql, timeout);
    // }

    // public String query() {
    //     if (ttl != null && System.currentTimeMillis() < expiredTime) {
    //         expiredTime = System.currentTimeMillis() + ttl * 1000;
    //         return cachedResult;
    //     }
    //     cachedResult = customQuery(promql, timeout);
    //     return cachedResult;
    // }
}

