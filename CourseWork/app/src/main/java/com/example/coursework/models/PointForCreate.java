package com.example.coursework.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "address",
        "latitude",
        "longitude"
})

public class PointForCreate {

    public PointForCreate(){

    }
    public PointForCreate(PathPoint point){
        name = point.name;
        address = point.address;
        latitude = point.latitude;
        longitude = point.longitude;
    }

    @JsonProperty("name")
    public String name;
    @JsonProperty("address")
    public String address;
    @JsonProperty("latitude")
    public double latitude;
    @JsonProperty("longitude")
    public double longitude;
}
