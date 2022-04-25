package com.example.coursework.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "address",
        "latitude",
        "longitude",
        "id",
        "pathId"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PathPoint {
    @JsonProperty("name")
    public String name;
    @JsonProperty("address")
    public String address;
    @JsonProperty("latitude")
    public double latitude;
    @JsonProperty("longitude")
    public double longitude;
    @JsonProperty("id")
    public int id;
    @JsonProperty("pathId")
    public int pathId;
}
