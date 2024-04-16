package com.rootCauseMonitorSystem.tools;
import com.rootCauseMonitorSystem.model.entity.MetricConfig;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class FileTool {
    public HashMap<String, String> readSimpleConfigFile(String filepath) {
        HashMap<String, String> metricMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("#") && !line.isEmpty()) {
                    String[] keyValue = line.split("=", 2);
                    if (keyValue.length == 2) {
                        String name = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        metricMap.put(name, value);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metricMap;
    }

    public HashMap<String, MetricConfig> parsePrometheusMetrics(String filepath, Map<String, String> metricMap) {
        Yaml yml = new Yaml();
        HashMap<String, MetricConfig> registeredMetricsMap = new HashMap<>();
        try {
            FileReader reader = new FileReader(filepath);
            BufferedReader buffer = new BufferedReader(reader);
            HashMap<String,Object> data = yml.load(buffer);
            metricMap.forEach((key, metricName) -> {
                HashMap<String, Object> item = (HashMap<String, Object>) data.get(metricName);
                String desc = (String) item.get("desc");
                String promql = (String) item.get("promql");
                String status = (String) item.get("status");
                Integer ttl = (Integer) item.get("ttl");
                Integer timeout = (Integer) item.get("timeout");
                if (Objects.equals(status, "enable")) {
                    MetricConfig metricConfig = new MetricConfig(metricName, promql, desc, ttl, timeout);
                    List<HashMap<String, Object>> metricsList = (List<HashMap<String, Object>>) item.get("metrics");
                    for (HashMap<String, Object> metric : metricsList) {
                        String usage = (String) metric.get("usage");
                        String name = (String) metric.get("name");
                        String label = (String) metric.get("label");
                        if (Objects.equals(usage, "LABEL")) {
                            metricConfig.addLabel(label, name);
                        }
                    }
                    registeredMetricsMap.put(metricName, metricConfig);
                }
            });
            reader.close();
            buffer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return registeredMetricsMap;
    }
}
