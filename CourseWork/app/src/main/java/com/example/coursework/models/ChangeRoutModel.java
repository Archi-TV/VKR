package com.example.coursework.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "authorUserId",
        "description",
        "name",
        "pathId",
        "points"
})


public class ChangeRoutModel {
    @JsonProperty("authorUserId")
    public int authorUserId;
    @JsonProperty("description")
    public String description;
    @JsonProperty("name")
    public String name;
    @JsonProperty("pathId")
    public int pathId;
    @JsonProperty("points")
    public ArrayList<PointForCreate> points;

    public int getAuthorUserId(){
        return authorUserId;
    }

    public String getDescription(){
        return description;
    }

    public String getName(){
        return name;
    }

    public int getPathId(){
        return pathId;
    }

    public ArrayList<PointForCreate> getPoints(){
        return points;
    }

    public void setAuthorUserId(int authorUserId) {
        this.authorUserId = authorUserId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    public void setPoints(ArrayList<PointForCreate> points) {
        this.points = points;
    }
}
