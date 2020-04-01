package com.mmutopia.pojo;

/**
 * Created by Utopia on 2020/4/1
 */
public class Sensor {
    private String sendorId;
    private long timestamp;
    private double termp;
    private long count;

    public Sensor() {
    }

    public Sensor(String sendorId, long timestamp, double termp, long count) {
        this.sendorId = sendorId;
        this.timestamp = timestamp;
        this.termp = termp;
        this.count = count;
    }

    public String getSendorId() {
        return sendorId;
    }

    public void setSendorId(String sendorId) {
        this.sendorId = sendorId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getTermp() {
        return termp;
    }

    public void setTermp(double termp) {
        this.termp = termp;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
