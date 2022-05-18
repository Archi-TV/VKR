package com.example.coursework.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "currentRating",
        "description",
        "id",
        "name",
        "userRate"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteModel {
    @JsonProperty("currentRating")
    public double currentRating;
    @JsonProperty("description")
    public String description;
    @JsonProperty("id")
    public int id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("userRate")
    public double userRate;
}
