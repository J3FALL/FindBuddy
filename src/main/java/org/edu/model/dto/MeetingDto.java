package org.edu.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.edu.util.LocalDateTimeDeserializer;
import org.edu.util.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class MeetingDto {

    private long id;
    private String title;
    private String description;
    private long longitude;
    private long latitude;
    private LocalDateTime createDate;
    private LocalDateTime startDate;
    private Long authorId;
    private String authorName;
    private Long stationId;
    private String stationName;
    private Long categoryId;
    private String categoryName;
    private int subscribersNum;

    private static final String[] months = {"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня", "Июля",
            "Августа", "Сентября", "Октября", "Ноября", "Дрекабря"};

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonProperty("start_date")
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String author) {
        this.authorName = author;
    }

    @JsonProperty("station_id")
    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    @JsonProperty("station_name")
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @JsonProperty("category_id")
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @JsonProperty("category_name")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSubscribersNum() {
        return subscribersNum;
    }

    public void setSubscribersNum(int subscribersNum) {
        this.subscribersNum = subscribersNum;
    }

    public String convertDate() {
        StringBuilder date = new StringBuilder();
        date.append(startDate.getDayOfMonth()).append(" ").append(months[startDate.getMonthValue() - 1]);
        return date.toString();
    }

    public String convertTime() {
        StringBuilder date = new StringBuilder();
        int hours;
        int minutes;
        if ((hours = this.startDate.getHour()) < 10)
            date.append(0);
        date.append(hours).append(":");
        if ((minutes = this.startDate.getMinute()) < 10)
            date.append(0);
        date.append(minutes);
        return date.toString();
    }

    @Override
    public String toString() {
        return "MeetingDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", createDate=" + createDate +
                ", startDate=" + startDate +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", stationId=" + stationId +
                ", stationName='" + stationName + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", subscribersNum=" + subscribersNum +
                '}';
    }
}
