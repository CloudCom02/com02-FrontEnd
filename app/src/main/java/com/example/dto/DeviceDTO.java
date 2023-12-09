package com.example.dto;

public class DeviceDTO {
    private String deviceName;
    private String category;
    private long batteryLevel;
    private boolean isNeedCharge;

    public DeviceDTO(String deviceName, String category, long batteryLevel, boolean isNeedCharge) {
        this.deviceName = deviceName;
        this.category = category;
        this.batteryLevel = batteryLevel;
        this.isNeedCharge = isNeedCharge;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getCategory() {
        return category;
    }

    public long getBatteryLevel() {
        return batteryLevel;
    }

    public boolean isNeedCharge() {
        return isNeedCharge;
    }
}