package com.example.com02;


public class BattDevicesDTO {
    String modelName;
    String category;
    String connectTypeRest;
    String battState;

    public BattDevicesDTO(String modelName, String category, String connectTypeRest, String battState){
        this.modelName = modelName;
        this.category = category;
        this.connectTypeRest = connectTypeRest;
        this.battState = battState;
    }

    public String getModelName(){
        return modelName;
    }

    public void setModelName(String modelName){
        this.modelName = modelName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getConnectTypeRest() {
        return connectTypeRest;
    }

    public void setConnectTypeRest(String connectTypeRest){
        this.connectTypeRest = connectTypeRest;
    }

    public String getBattState() {
        return battState;
    }

    public void setBattState(String battState){
        this.battState = battState;
    }
}
