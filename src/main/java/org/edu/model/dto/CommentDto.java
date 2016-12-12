package org.edu.model.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.edu.util.LocalDateTimeDeserializer;
import org.edu.util.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CommentDto {

    private long id;
    private LocalDateTime date;
    private String text;
    private String authorName;
    private long authorId;
    private String authorPhoto;
    private String meetingId;

    public CommentDto() {
    }

    public CommentDto(long id, LocalDateTime date, String text, String authorName, long authorId) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.authorName = authorName;
        this.authorId = authorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String convertDate() {
        return DateTimeFormatter
                .ofPattern("d MMM. HH:mm")
                .withLocale(new Locale("ru"))
                .format(this.date);
    }


    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
