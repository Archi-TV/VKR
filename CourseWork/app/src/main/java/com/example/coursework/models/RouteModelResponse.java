package com.example.coursework.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteModelResponse {
    public ArrayList<RouteModel> content;
}
