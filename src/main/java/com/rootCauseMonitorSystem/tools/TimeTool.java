package com.rootCauseMonitorSystem.tools;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class TimeTool {

    public LocalDateTime double2LocalDataTime(double time) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli((long) (time * 1000)), ZoneId.systemDefault());
        return localDateTime;
    }

    public String localDataTime2String(LocalDateTime time) {
        return String.valueOf(time.atZone(ZoneId.systemDefault()).toEpochSecond());
    }

    public LocalDateTime string2LocalDataTime(String time) {
        double timeDouble = Double.parseDouble(time);
        return double2LocalDataTime(timeDouble);
    }
}
