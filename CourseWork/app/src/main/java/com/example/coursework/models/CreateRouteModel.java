package com.example.coursework.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "description",
        "authorUserId",
        "points"
})

public class CreateRouteModel {
    @JsonProperty("name")
    public String name;
    @JsonProperty("description")
    public String description;
    @JsonProperty("authorUserId")
    public int authorUserId;
    @JsonProperty("points")
    public ArrayList<PointForCreate> points;
}
