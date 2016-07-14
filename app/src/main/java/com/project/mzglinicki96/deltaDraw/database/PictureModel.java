package com.project.mzglinicki96.deltaDraw.database;

/**
 * Created by mzglinicki.96 on 29.03.2016.
 */
public class PictureModel {

    private int id;
    private String name;
    private String author;
    private String points;
    private String date;

    public PictureModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setPoints(final String points) {
        this.points = points;
    }

    public String getPoints() {
        return points;
    }
}