package com.rootCauseMonitorSystem.scheduler;


import com.rootCauseMonitorSystem.executor.impl.NodeExportorExecImpl;
import com.rootCauseMonitorSystem.model.entity.MetricData;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Core {
    @Resource
    private NodeExportorExecImpl nodeExportorExec;

    @PostConstruct
    public void init() {
        nodeExportorExec.init();
    }

    /**
     * 任务：
     * 周四、
     // * 在getNodeMetricData中实现getMetricRangeData功能
     * 开始做ppt
     * 准备稿子
     * 周五、
     * 完成lab1无锁
     * 完成lab1有锁
      */
    @Scheduled(fixedRate = 10000)
    public void task0() {
        Long endTime = System.currentTimeMillis() / 1000;
        Long startTime = endTime - 10 * 60;
        String step = "300";
        Map<String, MetricData> nodeMetricDataMap = nodeExportorExec.getNodeMetricData(String.valueOf(startTime), String.valueOf(endTime), step);
        System.out.println("nodeMetricDataMap: " + nodeMetricDataMap);

        endTime = System.currentTimeMillis() / 1000;
        startTime = endTime;
        nodeMetricDataMap = nodeExportorExec.getNodeMetricData(String.valueOf(startTime), String.valueOf(endTime), null);
        System.out.println("nodeMetricDataMap: " + nodeMetricDataMap);
    }
}
