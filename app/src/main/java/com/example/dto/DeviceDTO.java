package com.example.dto;

public class DeviceDTO {
    private String deviceName;
    private String category;
    private Double batteryLevel;

    public DeviceDTO(String deviceName, String category) {
        this.deviceName = deviceName;
        this.category = category;
        this.batteryLevel = Double.valueOf(50);
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getCategory() {
        return category;
    }

    public Double getBatteryLevel() {
        return batteryLevel;
    }

}