package com.example.dto;

import android.widget.TextView;

import com.example.com02.R;

public class DeviceDetailDTO {
    private String deviceName;
    private String category;    //분류
    private Double mAh;  //배터리 용량
    private Double maximum_output;    //최대 출력 와트
    private String contents;    //충전 단자
    private String usingTime;     //전체 사용 가능 시간 (원래 type: Time)
    private String nowCapacity;
    private String averageDays;
    private Long capacityOfUserId;

    public DeviceDetailDTO(String deviceName, String category, Double mAh, Double maximum_output,
                           String contents, String usingTime, String nowCapacity, String averageDays,Long capacityOfUserId) {
        this.deviceName = deviceName;
        this.category = category;
        this.mAh = mAh;
        this.maximum_output = maximum_output;
        this.contents = contents;
        this.usingTime = usingTime;
        this.nowCapacity = nowCapacity;
        this.averageDays = averageDays;
        this.capacityOfUserId = capacityOfUserId;
    }

    public String getDeviceName() {
        return deviceName;
    }
    public String getCategory() {
        return category;
    }
    public Double getmAh(){return mAh;}
    public Double getMaximum_output() {
        return maximum_output;
    }
    public String getContents() {
        return contents;
    }
    public String getUsingTime() {
        return usingTime;
    }
    public String getNowCapacity() {
        return nowCapacity;
    }
    public String getAverageDays() {
        return averageDays;
    }
    public Long getCapacityOfUserId() { return capacityOfUserId;}

}
