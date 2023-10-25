package com.example.com02.Activity;
import java.util.HashMap;
import java.util.UUID;

public class SampleGattAttributes {

    private static HashMap<String, String> attributes = new HashMap();

    // Serial Port Service Class UUID 블루투스 시리얼 서비스
    public static String BT_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String UUID_DEVICE_INFORMATION = "0000ff01-0000-1000-8000-00805f9b34fb";
    public static String UUID_DEVICE_NAME = "0000ff02-0000-1000-8000-00805f9b34fb";
    public static String UUID_NOTIFICATION = "0000ff03-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        // Sample Characteristics.
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
