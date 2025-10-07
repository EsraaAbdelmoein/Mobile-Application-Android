package com.codelab.basics;

// Object DB ... see Room for Android Studio
// https://developer.android.com/training/data-storage/room
public class DataModel {

    private long id;
    private int number;
    private String name;
    private int powerLevel;
    private String description;
    private int accessCount;

// Change to reflect Pokemon
//    private long id;
//    private String Pokemon_Name;
//    private String Pokemon_Type;
//    private Integer Pokemon_Number;
    // ...

    public DataModel() {
        this.id = 0;
        this.number = 0;
        this.name = "Default Pokemon";
        this.powerLevel = 0;
        this.description = "No description";
        this.accessCount = 0;
    }

    public DataModel(long id, int number, String name, int powerLevel, String description, int accessCount) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.powerLevel = powerLevel;
        this.description = description;
        this.accessCount = accessCount;
    }

    public DataModel(int number, String name, int powerLevel) {
        this.id = 0;
        this.number = number;
        this.name = name;
        this.powerLevel = powerLevel;
        this.description = "No description";
        this.accessCount = 0;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + getId() +
                ", modelName='" + getModelName() + '\'' +
                ", modelNumber=" + getModelNumber() +
                '}';
    }

    // --- Getters & Setters ---
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(int accessCount) {
        this.accessCount = accessCount;
    }


    public void incrementAccessCount() {
        this.accessCount++;
    }


    public String getModelName() {
        return name;
    }

    public void setModelName(String modelName) {
        this.name = modelName;
    }

    public Integer getModelNumber() {
        return number;
    }

    public void setModelNumber(Integer modelNumber) {
        this.number = modelNumber;
    }
}