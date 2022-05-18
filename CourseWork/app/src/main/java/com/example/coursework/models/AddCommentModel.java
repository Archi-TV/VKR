package com.example.coursework.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddCommentModel {
    public int userId;
    public int pathId;
    public String text;
}
