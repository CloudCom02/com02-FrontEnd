package com.example.dto;

import java.sql.Time;

public class DeviceDetailDTO {
    private String deviceName;
    private String category;    //분류
    private Double entireCapacity;  //배터리 용량
    private Double maximumOutput;    //최대 출력 와트
    private String contents;    //충전 단자
    private String usingTime;     //전체 사용 가능 시간 (원래 type: Time)
    private String volt;       //전원 타입

    public DeviceDetailDTO(String deviceName, String category, Double entireCapacity, Double maximumOutput,
                           String contents, String usingTime, String volt) {
        this.deviceName = deviceName;
        this.category = category;
        this.entireCapacity = entireCapacity;
        this.maximumOutput = maximumOutput;
        this.contents = contents;
        this.usingTime = usingTime;
        this.volt = volt;
    }

    public String getDeviceName() {
        return deviceName;
    }
    public String getCategory() {
        return category;
    }
    public Double getEntireCapacity(){return entireCapacity;}
    public Double getMaximumOutput() {
        return maximumOutput;
    }
    public String getContents() {
        return contents;
    }
    public String getUsingTime() {
        return usingTime;
    }
    public String getVolt() {
        return volt;
    }

}
