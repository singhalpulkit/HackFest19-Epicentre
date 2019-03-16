package com.example.pulkit.epicentre.data;

public class DataPoint {
    public String lat;
    public String lng;
    public String disease;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    @Override
    public String toString() {
        return "DataPoint{" +
                "lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", disease='" + disease + '\'' +
                '}';
    }
}
