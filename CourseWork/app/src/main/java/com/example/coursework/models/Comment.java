package com.example.coursework.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {
    public Author author;
    public Date dateCreated;
    public int id;
    public int pathId;
    public String text;
    public int thumbUps;
    public boolean userHasLiked;
}
