package com.example.dto;

public class DeviceData {
    public DeviceData(String name, String contents, String imageURI) {
        this.name = name;
        this.contents = contents;
        this.imageURI = imageURI;
    }

    String name;
    String contents;
    String imageURI;

    public String getName() {
        return name;
    }

    public String getContents() {
        return contents;
    }

    public String getImageURI() {
        return imageURI;
    }
}
