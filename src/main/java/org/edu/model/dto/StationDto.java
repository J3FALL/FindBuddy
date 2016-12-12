package org.edu.model.dto;

/**
 * Created by Pavel on 27.11.2016.
 */
public class StationDto {
    private long id;
    private String name;
    private String color;

    public StationDto() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
